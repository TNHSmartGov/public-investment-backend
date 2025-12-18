package com.tnh.baseware.core.utils;

import com.tnh.baseware.core.resources.user.AuthenticationResource;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
public class PrivilegeExtractorUtils {

    private static final String API_PREFIX = "/api/v1";

    public static List<ExtractedPrivilege> extractAllPrivileges(String basePackage) {
        var allPrivileges = new ArrayList<ExtractedPrivilege>();
        var reflections = new Reflections(basePackage);

        var controllers = reflections.getTypesAnnotatedWith(RequestMapping.class);
        for (var controllerClass : controllers) {
            if (AuthenticationResource.class.equals(controllerClass)) continue;

            var controllerPrivileges = extractPrivileges(controllerClass);
            var wildcard = buildWildcardPrivilegeFromController(controllerClass);
            allPrivileges.addAll(controllerPrivileges);
            allPrivileges.add(wildcard);

            var entityName = controllerClass.getSimpleName().replace("Resource", "");
            log.debug(LogStyleHelper.debug("Extracted privileges for {}: {}"), entityName, controllerPrivileges.size());
        }
        return allPrivileges;
    }

    public static List<ExtractedPrivilege> extractPrivileges(Class<?> controllerClass) {
        var privileges = new ArrayList<ExtractedPrivilege>();

        if (controllerClass == null) return privileges;

        var basePath = getBasePath(controllerClass);
        var entityName = controllerClass.getSimpleName().replace("Resource", "");

        privileges.addAll(extractFromClass(controllerClass, basePath, entityName));

        var superClass = controllerClass.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            privileges.addAll(extractFromClass(superClass, basePath, entityName));
        }

        return privileges;
    }

    public static ExtractedPrivilege buildWildcardPrivilege(String entityName, String basePath) {
        var fullPath = normalizePath(basePath);
        var pattern = "^" + fullPath + "(/.*)?$";
        var name = entityName + " - ALL";
        var resourceName = entityName + " Management API";
        var description = "Full access to all endpoints under " + fullPath;
        return new ExtractedPrivilege("ALL", fullPath, pattern, name, resourceName, description);
    }

    public static ExtractedPrivilege buildWildcardPrivilegeFromController(Class<?> controllerClass) {
        var basePath = getBasePath(controllerClass);
        var entityName = controllerClass.getSimpleName().replace("Resource", "");
        return buildWildcardPrivilege(entityName, basePath);
    }

    private static List<ExtractedPrivilege> extractFromClass(Class<?> clazz, String basePath, String entityName) {
        var privilegeList = new ArrayList<ExtractedPrivilege>();

        for (var method : clazz.getDeclaredMethods()) {
            var httpMethod = getHttpMethod(method);
            var urlPattern = getUrlPattern(method);

            if (httpMethod.isPresent() && urlPattern.isPresent()) {
                var fullPath = normalizePath(basePath + urlPattern.get());
                var pathRegex = convertToRegexPattern(fullPath);
                var methodType = httpMethod.get();

                var readableMethod = convertToTitleCase(method.getName());
                var name = entityName + " - " + readableMethod;
                var description = Optional.ofNullable(method.getAnnotation(Operation.class))
                        .map(Operation::summary)
                        .filter(s -> !s.isBlank())
                        .orElse("No description");

                var privilege = new ExtractedPrivilege(
                        methodType,
                        fullPath,
                        methodType + ":" + pathRegex,
                        name,
                        entityName + " Management API",
                        description
                );

                privilegeList.add(privilege);
            }
        }

        return privilegeList;
    }

    private static String getBasePath(Class<?> controllerClass) {
        return Optional.ofNullable(controllerClass)
                .filter(c -> c.isAnnotationPresent(RequestMapping.class))
                .map(c -> c.getAnnotation(RequestMapping.class))
                .map(RequestMapping::value)
                .filter(v -> v.length > 0)
                .map(v -> resolveProperty(v[0]))
                .orElse("");
    }

    private static String resolveProperty(String path) {
        if (path.contains("${baseware.core.system.api-prefix}")) {
            return path.replace("${baseware.core.system.api-prefix}", API_PREFIX);
        }
        return path;
    }

    private static Optional<String> getHttpMethod(Method method) {
        return Optional.ofNullable(method.getAnnotation(RequestMapping.class))
                .map(RequestMapping::method)
                .filter(m -> m.length > 0)
                .map(m -> m[0].name())
                .or(() -> {
                    if (method.isAnnotationPresent(GetMapping.class)) return Optional.of("GET");
                    if (method.isAnnotationPresent(PostMapping.class)) return Optional.of("POST");
                    if (method.isAnnotationPresent(PutMapping.class)) return Optional.of("PUT");
                    if (method.isAnnotationPresent(DeleteMapping.class)) return Optional.of("DELETE");
                    if (method.isAnnotationPresent(PatchMapping.class)) return Optional.of("PATCH");
                    return Optional.empty();
                });
    }

    private static Optional<String> getUrlPattern(Method method) {
        return getMappingValue(method, GetMapping.class, GetMapping::value)
                .or(() -> getMappingValue(method, PostMapping.class, PostMapping::value))
                .or(() -> getMappingValue(method, PutMapping.class, PutMapping::value))
                .or(() -> getMappingValue(method, DeleteMapping.class, DeleteMapping::value))
                .or(() -> getMappingValue(method, PatchMapping.class, PatchMapping::value))
                .or(() -> getMappingValue(method, RequestMapping.class, RequestMapping::path));
    }

    private static <T extends Annotation> Optional<String> getMappingValue(
            Method method, Class<T> annotationClass, Function<T, String[]> valueExtractor) {

        if (method.isAnnotationPresent(annotationClass)) {
            T annotation = method.getAnnotation(annotationClass);
            if (annotation != null) {
                String[] values = valueExtractor.apply(annotation);
                if (values.length > 0) {
                    return Optional.of(getPath(values));
                }
            }
        }
        return Optional.empty();
    }

    private static String getPath(String[] paths) {
        return (paths.length > 0) ? paths[0] : "";
    }

    private static String convertToRegexPattern(String apiPattern) {
        if (apiPattern.endsWith("/***")) {
            return "^" + apiPattern.replace("/***", "/.*") + "$";
        }
        return "^" + apiPattern.replaceAll("\\{[^}]+}", "[^/]+") + "$";
    }

    private static String normalizePath(String path) {
        return path.replace("//", "/");
    }

    private static String convertToTitleCase(String camelCase) {
        var result = camelCase
                .replaceAll("([a-z])([A-Z])", "$1 $2")
                .replaceAll("([A-Z]+)([A-Z][a-z])", "$1 $2")
                .replace("_", " ")
                .replaceAll("\\s+", " ")
                .trim();
        return !result.isEmpty()
                ? result.substring(0, 1).toUpperCase() + result.substring(1)
                : result;
    }

    public static void main(String[] args) {
        var controllers = extractAllPrivileges("com.tnh.baseware.core.resources");
        for (var privilege : controllers) {
            log.info(LogStyleHelper.info("HTTP Method: {}"), privilege.httpMethod());
            log.info(LogStyleHelper.info("Full Path: {}"), privilege.fullPath());
            log.info(LogStyleHelper.info("API Pattern: {}"), privilege.apiPattern());
            log.info(LogStyleHelper.info("Name: {}"), privilege.name());
            log.info(LogStyleHelper.info("Resource Name: {}"), privilege.resourceName());
            log.info(LogStyleHelper.info("Description: {}"), privilege.description());
        }
    }

    public record ExtractedPrivilege(String httpMethod,
                                     String fullPath,
                                     String apiPattern,
                                     String name,
                                     String resourceName,
                                     String description) {
    }
}

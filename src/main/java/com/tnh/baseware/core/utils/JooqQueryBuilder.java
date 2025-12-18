package com.tnh.baseware.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class JooqQueryBuilder {
    final ObjectMapper objectMapper = new ObjectMapper();

    static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{(.*?)\\}");

    public QueryResultDTO buildQuery(String queryDefinitionJson, Map<String, Object> parameterValuesMap)
            throws Exception {
        log.debug("Bắt đầu xây dựng query từ JSON");

        // 1. PARSE JSON THÀNH DTO
        QueryRequestDTO request = objectMapper.readValue(queryDefinitionJson, QueryRequestDTO.class);
        QueryPart queryPart = request.getQuery();

        if (queryPart == null) {
            throw new IllegalArgumentException("Cấu trúc 'query' trong JSON không được để trống.");
        }

        StringBuilder sqlBuilder = new StringBuilder();
        List<Object> bindValues = new ArrayList<>();

        // 2. XÂY DỰNG TỪNG MỆNH ĐỀ SQL THEO ĐÚNG THỨ TỰ
        buildSelectClause(queryPart, sqlBuilder);
        buildFromClause(queryPart, sqlBuilder);
        buildJoinClause(queryPart, sqlBuilder, bindValues, parameterValuesMap);
        buildWhereClause(queryPart, sqlBuilder, bindValues, parameterValuesMap);
        buildGroupByClause(queryPart, sqlBuilder);
        buildHavingClause(queryPart, sqlBuilder, bindValues, parameterValuesMap);
        buildOrderByClause(queryPart, sqlBuilder);
        buildLimitOffsetClause(queryPart, sqlBuilder);

        // 3. TRẢ VỀ KẾT QUẢ
        QueryResultDTO result = new QueryResultDTO();
        result.setSql(sqlBuilder.toString().trim() + ";");
        result.setBindValues(bindValues);

        log.debug("Hoàn tất xây dựng query. SQL: {}", result.getSql());
        log.debug("Bind Values: {}", result.getBindValues());

        return result;
    }

    public static void main(String[] args) {
        try {
            String query = "{\"query\":{\"queryType\":\"SELECT\",\"select\":[{\"field\":\"province.code\",\"alias\":\"count_code\",\"function\":\"COUNT\",\"distinct\":false}],\"from\":{\"table\":\"province\",\"alias\":\"province\"},\"joins\":[],\"where\":[],\"groupBy\":[],\"having\":[],\"orderBy\":[],\"limit\":null,\"offset\":null},\"metadata\":{\"queryName\":\"\",\"description\":\"\",\"createdAt\":\"2025-09-09T05:00:12.038Z\",\"version\":\"1.0\"},\"parameters\":[]}";
            ObjectMapper objectMapper11 = new ObjectMapper();
            QueryRequestDTO request = objectMapper11.readValue(query, QueryRequestDTO.class);
            System.out.println(request);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }

    }
    // --- CÁC HÀM TRỢ GIÚP XÂY DỰNG MỆNH ĐỀ ---

    private void buildSelectClause(QueryPart query, StringBuilder sql) {
        sql.append("SELECT ");
        if (query.getSelect() == null || query.getSelect().isEmpty()) {
            sql.append("*");
            return;
        }
        String selectParts = query.getSelect().stream().map(s -> {
            StringBuilder part = new StringBuilder();
            if (s.isDistinct())
                part.append("DISTINCT ");
            if (s.getFunction() != null && !s.getFunction().isEmpty()) {
                part.append(s.getFunction()).append("(").append(s.getField()).append(")");
            } else {
                part.append(s.getField());
            }
            if (s.getAlias() != null && !s.getAlias().isEmpty()) {
                part.append(" AS \"").append(s.getAlias()).append("\"");
            }
            return part.toString();
        }).collect(Collectors.joining(", "));
        sql.append(selectParts);
    }

    private void buildFromClause(QueryPart query, StringBuilder sql) {
        if (query.getFrom() != null && query.getFrom().getTable() != null) {
            sql.append(" FROM ").append(query.getFrom().getTable());
            if (query.getFrom().getAlias() != null && !query.getFrom().getAlias().isEmpty()) {
                sql.append(" AS ").append(query.getFrom().getAlias());
            }
        } else {
            throw new IllegalArgumentException("Mệnh đề FROM không được để trống.");
        }
    }

    private void buildJoinClause(QueryPart query, StringBuilder sql, List<Object> bindValues,
            Map<String, Object> params) {
        List<JoinPart> joinList = query.getJoins();
        if (joinList == null || joinList.isEmpty())
            return;

        for (JoinPart join : joinList) {
            String joinKeyword;
            switch (join.getType().toUpperCase()) {
                case "LEFT_JOIN":
                    joinKeyword = " LEFT JOIN ";
                    break;
                case "RIGHT_JOIN":
                    joinKeyword = " RIGHT JOIN ";
                    break;
                case "INNER_JOIN":
                default:
                    joinKeyword = " INNER JOIN ";
                    break;
            }
            sql.append(joinKeyword).append(join.getTable());
            if (join.getAlias() != null && !join.getAlias().isEmpty()) {
                sql.append(" AS ").append(join.getAlias());
            }
            if (join.getOnConditions() != null && !join.getOnConditions().isEmpty()) {
                sql.append(" ON ");
                buildConditionsSql(join.getOnConditions(), sql, bindValues, params);
            }
        }
    }

    private void buildWhereClause(QueryPart query, StringBuilder sql, List<Object> bindValues,
            Map<String, Object> params) {
        List<WherePart> whereList = query.getWhere();
        if (whereList == null || whereList.isEmpty())
            return;
        sql.append(" WHERE ");
        buildConditionsSql(whereList, sql, bindValues, params);
    }

    private void buildHavingClause(QueryPart query, StringBuilder sql, List<Object> bindValues,
            Map<String, Object> params) {
        List<WherePart> havingList = query.getHaving();
        if (havingList == null || havingList.isEmpty())
            return;
        sql.append(" HAVING ");
        buildConditionsSql(havingList, sql, bindValues, params);
    }

    private void buildGroupByClause(QueryPart query, StringBuilder sql) {
        if (query.getGroupBy() != null && !query.getGroupBy().isEmpty()) {
            sql.append(" GROUP BY ").append(String.join(", ", query.getGroupBy()));
        }
    }

    private void buildOrderByClause(QueryPart query, StringBuilder sql) {
        List<OrderByPart> orderByList = query.getOrderBy();
        if (orderByList == null || orderByList.isEmpty())
            return;
        sql.append(" ORDER BY ");
        String orderByParts = orderByList.stream().map(orderBy -> {
            String direction = "DESC".equalsIgnoreCase(orderBy.getDirection()) ? " DESC" : " ASC";
            return orderBy.getField() + direction;
        }).collect(Collectors.joining(", "));
        sql.append(orderByParts);
    }

    private void buildLimitOffsetClause(QueryPart query, StringBuilder sql) {
        Integer limit = query.getLimit();
        Integer offset = query.getOffset();
        if (limit != null && limit > 0) {
            sql.append(" LIMIT ").append(limit);
            if (offset != null && offset >= 0) {
                sql.append(" OFFSET ").append(offset);
            }
        }
    }

    private void buildConditionsSql(List<WherePart> conditions, StringBuilder sql, List<Object> bindValues,
            Map<String, Object> params) {
        for (int i = 0; i < conditions.size(); i++) {
            WherePart w = conditions.get(i);
            if (i > 0) {
                sql.append(" ").append(w.getLogicalOperator() != null ? w.getLogicalOperator().toUpperCase() : "AND")
                        .append(" ");
            }
            sql.append(w.getField()).append(" ").append(w.getOperator()).append(" ");

            switch (w.getValueType().toLowerCase()) {
                case "parameter":
                    sql.append("?");
                    Matcher matcher = PLACEHOLDER_PATTERN.matcher(w.getValue());
                    if (matcher.find()) {
                        String paramKey = matcher.group(1);
                        if (!params.containsKey(paramKey)) {
                            throw new IllegalArgumentException(
                                    "Tham số bắt buộc '" + paramKey + "' không được cung cấp.");
                        }
                        bindValues.add(params.get(paramKey));
                    } else {
                        throw new IllegalArgumentException("Placeholder không hợp lệ: " + w.getValue());
                    }
                    break;
                case "field":
                    sql.append(w.getValue());
                    break;
                case "literal":
                default:
                    sql.append("?");
                    bindValues.add(w.getValue());
                    break;
            }
        }
    }

    // --- CÁC LỚP DTO LỒNG NHAU ĐỂ PARSE JSON ---
    @Getter
    @Setter
    public static class QueryResultDTO {
        private String sql;
        private List<Object> bindValues;
        // Getters & Setters
    }

    @Getter
    @Setter
    public static class QueryRequestDTO {
        private QueryPart query;
        private MetadataPart metadata;
        private List<ParameterPart> parameters;
        // Getters & Setters
    }

    @Getter
    @Setter
    public static class QueryPart {
        private String queryType;
        private List<SelectPart> select;
        private FromPart from;
        private List<JoinPart> joins;
        private List<WherePart> where;
        private List<String> groupBy;
        private List<WherePart> having;
        private List<OrderByPart> orderBy;
        private Integer limit;
        private Integer offset;
        // Getters & Setters
    }

    @Getter
    @Setter
    public static class SelectPart {
        private String field;
        private String alias;
        private String function;
        private boolean distinct;
        // Getters & Setters
    }

    @Getter
    @Setter
    public static class FromPart {
        private String table;
        private String alias;
        // Getters & Setters
    }

    @Getter
    @Setter
    public static class JoinPart {
        private String type;
        private String table;
        private String alias;
        private List<WherePart> onConditions;
        // Getters & Setters
    }

    @Getter
    @Setter
    public static class WherePart {
        private String field;
        private String operator;
        private String value;
        @JsonProperty("valueType")
        private String valueType;
        private String logicalOperator;
        // Getters & Setters
    }

    @Getter
    @Setter
    public static class OrderByPart {
        private String field;
        private String direction;
        // Getters & Setters
    }

    @Getter
    @Setter
    public static class MetadataPart {
        private String queryName;
        private String description;
        private String createdAt;
        private String version;
        // Getters & Setters
    }

    @Getter
    @Setter
    public static class ParameterPart {
        private String name;
        private String type;
        private String defaultValue;
        private boolean required;
        // Getters & Setters
    }
}

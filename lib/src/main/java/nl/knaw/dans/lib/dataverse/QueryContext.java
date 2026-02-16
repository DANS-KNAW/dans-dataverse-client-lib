/*
 * Copyright (C) 2021 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.lib.dataverse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Context for executing a SQL query with multiple sets of parameters. Automatically manages the lifecycle of the database connection and prepared statement. This class is intended to be used for
 * read-only queries and will set the connection to read-only mode.
 */
public final class QueryContext<T> implements AutoCloseable {
    private final Connection c;
    private final PreparedStatement ps;
    private final Function<ResultSet, T> defaultMapper;

    QueryContext(Connection c, String sql, Function<ResultSet, T> defaultMapper) throws SQLException {
        this.c = c;
        this.c.setReadOnly(true);
        this.defaultMapper = defaultMapper;
        this.ps = c.prepareStatement(sql);
    }

    /**
     * Executes the query for each set of parameters in the provided iterable, mapping each ResultSet row to an object of type T using the default mapper function. Automatically closes the ResultSet
     * after processing each set of parameters. Note that types of parameters in the parameter sets must be compatible with the SQL query.
     *
     * @param sets an iterable of parameter sets, where each parameter set is an array of objects corresponding to the parameters in the SQL query
     * @return a list of objects of type T resulting from executing the query for each set of parameters
     * @throws SQLException when database access fails
     */
    public List<T> executeFor(Iterable<Object[]> sets) throws SQLException {
        return executeFor(sets, defaultMapper);
    }

    /**
     * Executes the query for each set of parameters in the provided iterable, mapping each ResultSet row to an object of type T using the provided mapper function. Automatically closes the ResultSet
     * after processing each set of parameters. Note that types of parameters in the parameter sets must be compatible with the SQL query.
     *
     * @param sets   an iterable of parameter sets, where each parameter set is an array of objects corresponding to the parameters in the SQL query
     * @param mapper a function that maps a ResultSet row to an object of type U
     * @param <U>    the type of objects to return in the list
     * @return a list of objects of type U resulting from executing the query for each set of parameters
     * @throws SQLException when database access fails
     */
    public <U> List<U> executeFor(Iterable<Object[]> sets, Function<ResultSet, U> mapper) throws SQLException {
        var out = new ArrayList<U>();
        for (var p : sets) {
            for (int i = 0; i < p.length; i++)
                ps.setObject(i + 1, p[i]);
            try (var rs = ps.executeQuery()) {
                while (rs.next())
                    out.add(mapper.apply(rs));
            }
        }
        return out;
    }

    public void close() throws SQLException {
        ps.close();
        c.close();
    }
}
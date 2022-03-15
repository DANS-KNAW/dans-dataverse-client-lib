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
package nl.knaw.dans.lib.dataverse.model;

public class RoleAssignment {
    private String id;
    private String assignee;
    private String roleId;
    private String _roleAlias;
    private String definitionPointId;

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String get_roleAlias() {
        return _roleAlias;
    }

    public void set_roleAlias(String _roleAlias) {
        this._roleAlias = _roleAlias;
    }

    public String getDefinitionPointId() {
        return definitionPointId;
    }

    public void setDefinitionPointId(String definitionPointId) {
        this.definitionPointId = definitionPointId;
    }
}

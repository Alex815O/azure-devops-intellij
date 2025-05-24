// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See License.txt in the project root.

package com.microsoft.alm.plugin.context.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.microsoft.alm.client.AlmHttpClientBase;
import com.microsoft.alm.client.model.ApiResourceVersion;
import com.microsoft.alm.client.model.NameValueCollection;
import com.microsoft.alm.sourcecontrol.webapi.GitHttpClient;
import com.microsoft.alm.sourcecontrol.webapi.model.GitCommitChanges;
import com.microsoft.alm.sourcecontrol.webapi.model.GitPullRequest;

import javax.ws.rs.client.Client;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Extending GitHttpClient to include new API calls
 */
public class GitHttpClientEx extends GitHttpClient {

    public GitHttpClientEx(final Client jaxrsClient, final URI baseUrl) {
        super(jaxrsClient, baseUrl);
    }

    /**
     * [Preview API 3.1-preview.1] Create a git pull request
     *
     * @param gitPullRequestToCreate
     * @param repositoryId
     * @param linkBranchWorkitems
     * @param linkCommitWorkitems
     * @return GitPullRequest
     */
    public GitPullRequest createPullRequest(
            final GitPullRequest gitPullRequestToCreate,
            final UUID project,
            final UUID repositoryId,
            final Boolean linkBranchWorkitems,
            final Boolean linkCommitWorkitems) {

        final UUID locationId = UUID.fromString("9946fd70-0d40-406e-b686-b4744cbbcc37");
        final ApiResourceVersion apiVersion = new ApiResourceVersion("3.1-preview.1");

        final Map<String, Object> routeValues = new HashMap<String, Object>();
        routeValues.put("project", project);
        routeValues.put("repositoryId", repositoryId);

        final NameValueCollection queryParameters = new NameValueCollection();
        queryParameters.addIfNotNull("linkBranchWorkitems", linkBranchWorkitems);
        queryParameters.addIfNotNull("linkCommitWorkitems", linkCommitWorkitems);

        final Object httpRequest = super.createRequest(AlmHttpClientBase.HttpMethod.POST,
                locationId,
                routeValues,
                apiVersion,
                gitPullRequestToCreate,
                APPLICATION_JSON_TYPE,
                queryParameters,
                APPLICATION_JSON_TYPE);

        return super.sendRequest(httpRequest, GitPullRequest.class);
    }

    public GitCommitChanges getChanges(String commitId, UUID repositoryId, Integer top, Integer skip) {
        UUID locationId = UUID.fromString("5bf884f5-3e07-42e9-afb8-1b872267bf16");
        ApiResourceVersion apiVersion = new ApiResourceVersion("7.1");
        Map<String, Object> routeValues = new HashMap();
        routeValues.put("commitId", commitId);
        routeValues.put("repositoryId", repositoryId);
        NameValueCollection queryParameters = new NameValueCollection();
        queryParameters.addIfNotNull("top", top);
        queryParameters.addIfNotNull("skip", skip);
        Object httpRequest = super.createRequest(HttpMethod.GET, locationId, routeValues, apiVersion, queryParameters, APPLICATION_JSON_TYPE);
        String jsonEntity = super.sendRequest(httpRequest, String.class);
        try {
            return deserializeJson(jsonEntity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private GitCommitChanges deserializeJson(final String rawJson) throws JsonProcessingException {
        ObjectMapper mapper = JsonMapper.builder()
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .build();

        var entity = mapper.readValue(
                rawJson,
                GitCommitChanges.class
        );
        return (GitCommitChanges) entity;
    }

}

/*
 * Copyright (C) 2005 - 2014 Jaspersoft Corporation. All rights  reserved.
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased  a commercial license agreement from Jaspersoft,
 * the following license terms  apply:
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License  as
 * published by the Free Software Foundation, either version 3 of  the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero  General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public  License
 * along with this program.&nbsp; If not, see <http://www.gnu.org/licenses/>.
 */

package com.jaspersoft.jasperserver.jaxrs.client.apiadapters.jobs;

import com.jaspersoft.jasperserver.jaxrs.client.apiadapters.AbstractAdapter;
import com.jaspersoft.jasperserver.jaxrs.client.core.JerseyRequestBuilder;
import com.jaspersoft.jasperserver.jaxrs.client.core.SessionStorage;
import com.jaspersoft.jasperserver.jaxrs.client.core.exceptions.handling.DefaultErrorHandler;
import com.jaspersoft.jasperserver.jaxrs.client.core.exceptions.handling.ErrorHandler;
import com.jaspersoft.jasperserver.jaxrs.client.core.operationresult.OperationResult;
import com.jaspersoft.jasperserver.jaxrs.client.dto.jobs.JobExtension;
import com.jaspersoft.jasperserver.jaxrs.client.dto.jobs.JobState;

import static com.jaspersoft.jasperserver.jaxrs.client.core.JerseyRequestBuilder.buildRequest;

public class SingleJobOperationsAdapter extends AbstractAdapter {

    private final String jobId;
    private ErrorHandler errorHandler;

    public SingleJobOperationsAdapter(SessionStorage sessionStorage, String jobId) {
        super(sessionStorage);
        this.jobId = jobId;
        this.errorHandler = new DefaultErrorHandler();
    }

    public OperationResult<JobExtension> get(){
        JerseyRequestBuilder<JobExtension> builder =
                buildRequest(sessionStorage, JobExtension.class, new String[]{"/jobs", jobId}, errorHandler);
        builder.setAccept("application/job+json");
        return builder.get();
    }

    public OperationResult<JobState> state(){
        return buildRequest(sessionStorage, JobState.class, new String[]{"/jobs", jobId, "/state"}, errorHandler)
                .get();
    }

    public OperationResult<JobExtension> update(JobExtension job){
        JerseyRequestBuilder<JobExtension> builder =
                buildRequest(sessionStorage, JobExtension.class, new String[]{"/jobs", jobId}, new JobValidationErrorHandler());
        builder.setContentType("application/job+json");
        builder.setAccept("application/job+json");

        return builder.post(job);
    }

    public OperationResult delete() {
        return buildRequest(sessionStorage, Object.class, new String[]{"/jobs", jobId}, errorHandler)
                .delete();
    }
}

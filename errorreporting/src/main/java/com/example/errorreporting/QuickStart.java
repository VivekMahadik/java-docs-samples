/**
 * Copyright 2017 Google Inc.
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.errorreporting;

//[START errorreporting_quickstart]
import com.google.cloud.ServiceOptions;
import com.google.cloud.errorreporting.v1beta1.ReportErrorsServiceClient;
import com.google.devtools.clouderrorreporting.v1beta1.ErrorContext;
import com.google.devtools.clouderrorreporting.v1beta1.ProjectName;
import com.google.devtools.clouderrorreporting.v1beta1.ReportedErrorEvent;
import com.google.devtools.clouderrorreporting.v1beta1.SourceLocation;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Snippet demonstrates using the Google Cloud Error Reporting API to report a custom error event.
 */
public class QuickStart {
  public static void main(String[] args) throws Exception {

    // Google Cloud Platform Project ID
    String projectId = (args.length > 0) ? args[0] : ServiceOptions.getDefaultProjectId();
    ProjectName projectName = ProjectName.create(projectId);

    // Instantiate an Error Reporting Client
    try (ReportErrorsServiceClient reportErrorsServiceClient = ReportErrorsServiceClient.create()) {

      // Custom error events require an error reporting location as well.
      ErrorContext errorContext = ErrorContext.newBuilder()
          .setReportLocation(SourceLocation.newBuilder()
                  .setFilePath("Test.java")
                  .setLineNumber(10)
                  .setFunctionName("myMethod")
                  .build())
          .build();
      //Report a custom error event
      ReportedErrorEvent customErrorEvent = ReportedErrorEvent.getDefaultInstance()
              .toBuilder()
              .setMessage("custom error event")
              .setContext(errorContext)
              .build();
      // Report an event synchronously, use .reportErrorEventCallable for asynchronous reporting.
      reportErrorsServiceClient.reportErrorEvent(projectName, customErrorEvent);

      // Report a stack trace from an exception using the API
      // Stack trace / exception reporting does not require source location
      Exception e = new Exception("custom event reported using the API");
      StringWriter sw = new StringWriter();
      e.printStackTrace(new PrintWriter(sw));
      ReportedErrorEvent stackTraceEvent = ReportedErrorEvent.getDefaultInstance()
          .toBuilder()
          .setMessage(sw.toString())
          .build();
      reportErrorsServiceClient.reportErrorEvent(projectName, stackTraceEvent);
    }
  }
}
// [END errorreporting_quickstart]

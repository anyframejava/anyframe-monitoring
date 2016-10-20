/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
/* 
 * ApplicationMessages.java Date created: Jan 12, 2008
 * Last modified by: $Author: mike $ 
 * $Revision: 14572 $ $Date: 2009-06-10 20:06:30 +0900 (수, 10 6월 2009) $
 */

package net.sf.infrared2.gwt.client.i18n;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Dictionary;

/**
 * <b>ApplicationMessages</b><p>
 * This interface provides i18n messages via GWT i18n API.
 * 
 * @author Andruschuk Borislav
 */
public class ApplicationMessages {// extends Messages {

	private static Dictionary RESOURCES = Dictionary.getDictionary("messages_def");

	/**
	 * This instance holds all application messages.
	 */
	public static final ApplicationMessages MESSAGES = (ApplicationMessages) GWT.create(ApplicationMessages.class);

	/**
	 * Gets the message.
	 * 
	 * @param key the key
	 * 
	 * @return the message
	 */
	private String getMessage(String key) {
		return RESOURCES.get(key);
	}

	/**
	 * @return performance information label text.
	 */
	public String performanceInformationLabel() {
		return getMessage("performanceInformationLabel");
	}

	/**
	 * @return select application label.
	 */
	public String selectApplicationLabel() {
		return getMessage("selectApplicationLabel");
	}

	/**
	 * @return select application in small char format for Select Application
	 *         dialog box.
	 */
	public String selectApplicationLabelLower() {
		return getMessage("selectApplicationLabelLower");
	}

	/**
	 * @return select applications in small char format for Select Application
	 *         dialog box.
	 */
	public String selectApplicationsLabelLower() {
		return getMessage("selectApplicationsLabelLower");
	}

	/**
	 * @return application instance label.
	 */
	public String applicationInstanceLabel() {
		return getMessage("applicationInstanceLabel");
	}

	/**
	 * @return navigation panel label.
	 */
	public String navigationPanel() {
		return getMessage("navigationPanel");
	}

	/**
	 * @return inclusive mode label.
	 */
	public String inclusiveModeLabel() {
		return getMessage("inclusiveModeLabel");
	}

	/**
	 * @return exclusive mode label.
	 */
	public String exclusiveModeLabel() {
		return getMessage("exclusiveModeLabel");
	}

	/**
	 * @return footer content.
	 */
	public String footerContentLabel() {
		return getMessage("footerContentLabel");
	}

	/**
	 * @return return tab name for general information tab.
	 */
	public String generalInfo() {
		return getMessage("generalInfo");
	}

	/**
	 * @return return tab name for SQL tab.
	 */
	public String sql() {
		return getMessage("sql");
	}

	/**
	 * @return return tab name for JDBC tab.
	 */
	public String jdbc() {
		return getMessage("jdbc");
	}

	/**
	 * @return return tab name for Trace tab.
	 */
	public String trace() {
		return getMessage("trace");
	}

	/**
	 * @return return tab name for Summary tab.
	 */
	public String summary() {
		return getMessage("summary");
	}

	/**
	 * @return text for DuplicateColumnException.
	 */
	public String duplicateColumnExceptionLabel() {
		return getMessage("duplicateColumnExceptionLabel");
	}

	/**
	 * @return label for stack panel.
	 */
	public String absoluteModuleStackLabel() {
		return getMessage("absoluteModuleStackLabel");
	}

	/**
	 * @return label for stack panel.
	 */
	public String lastInvocationStackLabel() {
		return getMessage("lastInvocationStackLabel");
	}

	/**
	 * @return label for stack panel.
	 */
	public String hierarchicalModuleStackLabel() {
		return getMessage("hierarchicalModuleStackLabel");
	}

	/**
	 * @return label for mask while loading.
	 */
	public String loadingMask() {
		return getMessage("loadingMask");
	}

	/**
	 * @return application logo title.
	 */
	public String applicationLogoTitle() {
		return getMessage("applicationLogoTitle");
	}

	/**
	 * @return name for details tab.
	 */
	public String details() {
		return getMessage("details");
	}

	/**
	 * @return name for operation by name tab.
	 */
	public String operationByNameTitle() {
		return getMessage("operationByNameTitle");
	}

	/**
	 * @return name for operation by count tab.
	 */
	public String operationByCountTitle() {
		return getMessage("operationByCountTitle");
	}

	/**
	 * @return name label.
	 */
	public String name() {
		return getMessage("name");
	}

	/**
	 * @return call hierarchy label.
	 */
	public String callHierarchy() {
		return getMessage("callHierarchy");
	}

	/**
	 * @return statistic label.
	 */
	public String statistic() {
		return getMessage("statistic");
	}

	/**
	 * @return refresh link label value.
	 */
	public String refreshLink() {
		return getMessage("refreshLink");
	}

	/**
	 * Reset link.
	 * 
	 * @return the string
	 */
	public String resetLink() {
		return getMessage("resetLink");
	}

	/**
	 * @return header link stack separator value.
	 */
	public String headerLinksSeparator() {
		return getMessage("headerLinksSeparator");
	}

	/**
	 * @return cancel button label text value.
	 */
	public String cancelButtonLabel() {
		return getMessage("cancelButtonLabel");
	}

	/**
	 * @return Ok button text label value.
	 */
	public String okButtonLabel() {
		return getMessage("okButtonLabel");
	}

	/**
	 * @return Devided by Appl text label value.
	 */
	public String dividedByApplLabel() {
		return getMessage("dividedByApplLabel");
	}

	/**
	 * @return Live data label text value.
	 */
	public String liveDataLabelText() {
		return getMessage("liveDataLabelText");
	}

	/**
	 * @return Archive data label text value.
	 */
	public String archiveDataLabelText() {
		return getMessage("archiveDataLabelText");
	}

	/**
	 * @return Period label text value.
	 */
	public String periodLabelText() {
		return getMessage("periodLabelText");
	}

	/**
	 * @return Select monitoring period select label text value.
	 */
	public String selectMonitoringPeriodSelectLabelText() {
		return getMessage("selectMonitoringPeriodSelectLabelText");
	}

	/**
	 * @return From label text value.
	 */
	public String fromLabelText() {
		return getMessage("fromLabelText");
	}

	/**
	 * @return To label text value.
	 */
	public String toLabelText() {
		return getMessage("toLabelText");
	}

	/**
	 * @return no application selected label.
	 */
	public String noApplicationSelectedLabel() {
		return getMessage("noApplicationSelectedLabel");
	}

	/**
	 * @return layer label.
	 */
	public String layer() {
		return getMessage("layer");
	}

	/**
	 * @return total time label.
	 */
	public String totalTime() {
		return getMessage("totalTime");
	}

	/**
	 * @return count label.
	 */
	public String count() {
		return getMessage("count");
	}

	/**
	 * @return layers by total time.
	 */
	public String layersByTotalTime() {
		return getMessage("layersByTotalTime");
	}

	/**
	 * @return layers by count.
	 */
	public String layersByCount() {
		return getMessage("layersByCount");
	}

	/**
	 * @return ID label.
	 */
	public String ID() {
		return getMessage("ID");
	}

	/**
	 * @return query label.
	 */
	public String query() {
		return getMessage("query");
	}

	/**
	 * @return total inclusive time.
	 */
	public String totalInclusiveTime() {
		return getMessage("totalInclusiveTime");
	}

	/**
	 * @return max inclusive time.
	 */
	public String maxInclusiveTime() {
		return getMessage("maxInclusiveTime");
	}

	/**
	 * @return min inclusive time.
	 */
	public String minInclusiveTime() {
		return getMessage("minInclusiveTime");
	}

	/**
	 * @return total exclusive time.
	 */
	public String totalExclusiveTime() {
		return getMessage("totalExclusiveTime");
	}

	/**
	 * @return max exclusive time.
	 */
	public String maxExclusiveTime() {
		return getMessage("maxExclusiveTime");
	}

	/**
	 * @return min exclusive time.
	 */
	public String minExclusiveTime() {
		return getMessage("minExclusiveTime");
	}

	/**
	 * @return first execution time.
	 */
	public String firstExecutionTime() {
		return getMessage("firstExecutionTime");
	}

	/**
	 * @return last execution time.
	 */
	public String lastExecutionTime() {
		return getMessage("lastExecutionTime");
	}

	/**
	 * @return first execution inclusive time.
	 */
	public String firstExecutionInclusiveTime() {
		return getMessage("firstExecutionInclusiveTime");
	}

	/**
	 * @return last execution inclusive time.
	 */
	public String lastExecutionInclusiveTime() {
		return getMessage("lastExecutionInclusiveTime");
	}

	/**
	 * @return first execution exclusive time.
	 */
	public String firstExecutionExclusiveTime() {
		return getMessage("firstExecutionExclusiveTime");
	}

	/**
	 * @return last execution exclusive time.
	 */
	public String lastExecutionExclusiveTime() {
		return getMessage("lastExecutionExclusiveTime");
	}

	/**
	 * @return operation name.
	 */
	public String operationName() {
		return getMessage("operationName");
	}

	/**
	 * @return average time.
	 */
	public String averageTime() {
		return getMessage("averageTime");
	}

	/**
	 * @return adjusted average time.
	 */
	public String adjustedAverageTime() {
		return getMessage("adjustedAverageTime");
	}

	/**
	 * @return minimum execution time.
	 */
	public String minimumExecutionTime() {
		return getMessage("minimumExecutionTime");
	}

	/**
	 * @return maximum execution time.
	 */
	public String maximumExecutionTime() {
		return getMessage("maximumExecutionTime");
	}

	/**
	 * @return top SQL queries by execution count.
	 */
	public String topSQLqueriesByExecutionCount() {
		return getMessage("topSQLqueriesByExecutionCount");
	}

	/**
	 * @return top SQL queries by average execution time.
	 */
	public String topSQLqueriesByAverageExecutionTime() {
		return getMessage("topSQLqueriesByAverageExecutionTime");
	}

	/**
	 * @return prepare text.
	 */
	public String prepare() {
		return getMessage("prepare");
	}

	/**
	 * @return execute label.
	 */
	public String execute() {
		return getMessage("execute");
	}

	/**
	 * @return total text.
	 */
	public String total() {
		return getMessage("total");
	}

	/**
	 * @return SQL query text.
	 */
	public String sqlQuery() {
		return getMessage("sqlQuery");
	}

	/**
	 * @return minimum time text.
	 */
	public String minimumTime() {
		return getMessage("minimumTime");
	}

	/**
	 * @return maximum time text.
	 */
	public String maximumTime() {
		return getMessage("maximumTime");
	}

	/**
	 * @return API Name.
	 */
	public String apiName() {
		return getMessage("apiName");
	}

	/**
	 * @return operations by count.
	 */
	public String operationsByCount() {
		return getMessage("operationsByCount");
	}

	/**
	 * @return operations by total time.
	 */
	public String operationsByTotalTime() {
		return getMessage("operationsByTotalTime");
	}

	/**
	 * @return Archive date range error.
	 */
	public String archiveDateRangeError() {
		return getMessage("archiveDateRangeError");
	}

	/**
	 * @return application error dialog title.
	 */
	public String errorDlgTitleLabel() {
		return getMessage("errorDlgTitleLabel");
	}

	/**
	 * @return application error dialog message.
	 */
	public String errorDlgMessageLabel() {
		return getMessage("errorDlgMessageLabel");
	}

	/**
	 * @return other operations message.
	 */
	public String otherOperationsGraphic() {
		return getMessage("otherOperationsGraphic");
	}

	/**
	 * @return no information label.
	 */
	public String noInformationAvailable() {
		return getMessage("noInformationAvailable");
	}

	/**
	 * @return no information available for this tab
	 */
	public String noInformationAvailableForTab() {
		return getMessage("noInformationAvailableForTab");
	}

	/**
	 * @return report header link label.
	 */
	public String reportLink() {
		return getMessage("reportLink");
	}

	/**
	 * @return back button label text.
	 */
	public String backButtonLabel() {
		return getMessage("backButtonLabel");
	}

	/**
	 * @return next button label text.
	 */
	public String nextButtonLabel() {
		return getMessage("nextButtonLabel");
	}

	/**
	 * @return finish button label text.
	 */
	public String finishButtonLabel() {
		return getMessage("finishButtonLabel");
	}

	/**
	 * @return report wizard header text.
	 */
	public String reportWizardHeader() {
		return getMessage("reportWizardHeader");
	}

	/**
	 * @return select desired report format.
	 */
	public String selectDesiredRepoFormat() {
		return getMessage("selectDesiredRepoFormat");
	}

	/**
	 * @return excel format text.
	 */
	public String excelFormat() {
		return getMessage("excelFormat");
	}

	/**
	 * @return pdf format text.
	 */
	public String pdfFormat() {
		return getMessage("pdfFormat");
	}

	/**
	 * @return html format text.
	 */
	public String htmlFormat() {
		return getMessage("htmlFormat");
	}

	/**
	 * @return csv format text.
	 */
	public String csvFormat() {
		return getMessage("csvFormat");
	}

	/**
	 * @return absolute module mode.
	 */
	public String absoluteModuleMode() {
		return getMessage("absoluteModuleMode");
	}

	/**
	 * @return hierarchical module mode.
	 */
	public String hierarchicalModuleMode() {
		return getMessage("hierarchicalModuleMode");
	}

	/**
	 * @return applications text.
	 */
	public String applications() {
		return getMessage("applications");
	}

	/**
	 * @return currently selected text.
	 */
	public String currentlySelected() {
		return getMessage("currentlySelected");
	}

	/**
	 * @return is currently selected text.
	 */
	public String isCurrentlySelected() {
		return getMessage("isCurrentlySelected");
	}

	/**
	 * @return all displayed text.
	 */
	public String allDisplayed() {
		return getMessage("allDisplayed");
	}

	/**
	 * @return inclusive report mode.
	 */
	public String inclusiveReportMode() {
		return getMessage("inclusiveReportMode");
	}

	/**
	 * @return exclusive report mode.
	 */
	public String exclusiveReportMode() {
		return getMessage("exclusiveReportMode");
	}

	/**
	 * @return inclusive and exclusive report mode.
	 */
	public String inclusiveExclusiveReportMode() {
		return getMessage("inclusiveExclusiveReportMode");
	}

	/**
	 * @return label for sql view summary tab listbox.
	 */
	public String selectTopCriterion() {
		return getMessage("selectTopCriterion");
	}

	/**
	 * @return list box items label.
	 */
	public String byAverageExecutionTimeItem() {
		return getMessage("byAverageExecutionTimeItem");
	}

	/**
	 * @return list box items label.
	 */
	public String byExecutionCountItem() {
		return getMessage("byExecutionCountItem");
	}

	/**
	 * @return the sequences count.
	 */
	public String sequencesCount() {
		return getMessage("sequencesCount");
	}

	/**
	 * @return the sequences total time.
	 */
	public String sequencesTotalTime() {
		return getMessage("sequencesTotalTime");
	}

	/**
	 * @return Count of calls label.
	 */
	public String countOfCalls() {
		return getMessage("countOfCalls");
	}

	/**
	 * @return time of first execution label.
	 */
	public String timeOfFirstExecution() {
		return getMessage("timeOfFirstExecution");
	}

	/**
	 * @return time of last execution label.
	 */
	public String timeOfLastExecution() {
		return getMessage("timeOfLastExecution");
	}

	/**
	 * @return include modes text.
	 */
	public String includeModes() {
		return getMessage("includeModes");
	}

	/**
	 * @return the include levels text label.
	 */
	public String includeLevels() {
		return getMessage("includeLevels");
	}

	/**
	 * @return inclusive information label text.
	 */
	public String includeInformation() {
		return getMessage("includeInformation");
	}

	/**
	 * @return diagrams label text.
	 */
	public String diagrams() {
		return getMessage("diagrams");
	}

	public String topQueriesOperationsCmbLabel() {
		return getMessage("topQueriesOperationsCmbLabel");
	}

	/**
	 * @return color text.
	 */
	public String color() {
		return getMessage("color");
	}

	/**
	 * @return invocation sequence column name.
	 */
	public String invocationSequence() {
		return getMessage("invocationSequence");
	}

	/**
	 * @return merged information text.
	 */
	public String mergedInformation() {
		return getMessage("mergedInformation");
	}

	/**
	 * @return CSV format only info.
	 */
	public String csvFormatOnlyInfo() {
		return getMessage("csvFormatOnlyInfo");
	}

	/**
	 * @return include mode text.
	 */
	public String includeModeLabel() {
		return getMessage("includeModeLabel");
	}

	/**
	 * @return Layer level - SQL layers.
	 */
	public String layerLevelSQLLayers() {
		return getMessage("layerLevelSQLLayers");
	}

	/**
	 * @return Top 10 queries by Average Execute Time.
	 */
	public String topQueriesAverageExecuteTime() {
		return getMessage("topQueriesAverageExecuteTime");
	}

	/**
	 * @return Top 10 queries by Execution Count.
	 */
	public String topQueriesExecutionCount() {
		return getMessage("topQueriesExecutionCount");
	}

	/**
	 * @return Queries by Average Execution Time.
	 */
	public String queriesByAverageExecutionTime() {
		return getMessage("queriesByAverageExecutionTime");
	}

	/**
	 * @return Queries by Execution Count.
	 */
	public String queriesByExecutionCount() {
		return getMessage("queriesByExecutionCount");
	}

	/**
	 * @return Layer level (Inclusive).
	 */
	public String layerLevelInclusive() {
		return getMessage("layerLevelInclusive");
	}

	/**
	 * @return Layer level (Exclusive).
	 */
	public String layerLevelExclusive() {
		return getMessage("layerLevelExclusive");
	}

	/**
	 * @return Top 10 operations by Adjusted Average Time.
	 */
	public String topOperationsByAdjustedAverageTime() {
		return getMessage("topOperationsByAdjustedAverageTime");
	}

	/**
	 * @return Top 10 operations by Total Time.
	 */
	public String topOperationsByTotalTime() {
		return getMessage("topOperationsByTotalTime");
	}

	/**
	 * @return Top 10 operations by Count.
	 */
	public String topOperationsByCount() {
		return getMessage("topOperationsByCount");
	}

	/**
	 * @return HTTP label.
	 */
	public String httpLabel() {
		return getMessage("httpLabel");
	}

	/**
	 * @return Application level.
	 */
	public String applicationLavel() {
		return getMessage("applicationLavel");
	}

	/**
	 * @return String In order to generate report, please select some
	 *         application(s) from select application dialog.
	 */
	public String applNeedToBeSelected() {
		return getMessage("applNeedToBeSelected");
	}

	/**
	 * @return String Information.
	 */
	public String informationTitle() {
		return getMessage("informationTitle");
	}

	/**
	 * @return desired repo format.
	 */
	public String desiredRepoFormat() {
		return getMessage("desiredRepoFormat");
	}

	/**
	 * @return top operations queries.
	 */
	public String topOperationsQueries() {
		return getMessage("topOperationsQueries");
	}

	/**
	 * @return asking download report string.
	 */
	public String askingDownloadReport() {
		return getMessage("askingDownloadReport");
	}

	/**
	 * @return complete string.
	 */
	public String complete() {
		return getMessage("complete");
	}

	/**
	 * @return report generation complete.
	 */
	public String reportGenerationComplete() {
		return getMessage("reportGenerationComplete");
	}

	/**
	 * @return download string.
	 */
	public String download() {
		return getMessage("download");
	}

	/**
	 * @return format string.
	 */
	public String format() {
		return getMessage("format");
	}

	/**
	 * @return report include modes text.
	 */
	public String reportIncludeModes() {
		return getMessage("reportIncludeModes");
	}

	/**
	 * @return report include levels text.
	 */
	public String reportIncludeLevels() {
		return getMessage("reportIncludeLevels");
	}

	/**
	 * @return layer level.
	 */
	public String layerLevel() {
		return getMessage("layerLevel");
	}

	/**
	 * @return report operations queries text.
	 */
	public String reportOperationsQueries() {
		return getMessage("reportOperationsQueries");
	}

	/**
	 * @return Help.
	 */
	public String help() {
		return getMessage("help");
	}

	/**
	 * @return title for application system error.
	 */
	public String systemErrorTitle() {
		return getMessage("systemErrorTitle");
	}

	/**
	 * @return help tip.
	 */
	public String helpTipContinue() {
		return getMessage("helpTipContinue");
	}

	/**
	 * @return help tip.
	 */
	public String helpTipClean() {
		return getMessage("helpTipClean");
	}

	/**
	 * @return help tip.
	 */
	public String helpTipNetwork() {
		return getMessage("helpTipNetwork");
	}

	/**
	 * @return help tip.
	 */
	public String helpTipAdmin() {
		return getMessage("helpTipAdmin");
	}

	/**
	 * @return help tip summary.
	 */
	public String helpTipSummary() {
		return getMessage("helpTipSummary");
	}

	/**
	 * @return application system error summary.
	 */
	public String unexpectedException() {
		return getMessage("unexpectedException");
	}

	/**
	 * @return application system error details when connection to server
	 *         problem.
	 */
	public String serverNotRespond() {
		return getMessage("serverNotRespond");
	}

	/**
	 * @return show labels in graph
	 */
	public String showLabelsInGraph() {
		return getMessage("showLabelsInGraph");
	}
	
	/**
	 * Generate report.
	 * 
	 * @return the string
	 */
	public String generateReport(){
	    return getMessage("generateReport");
	}
	
	/**
	 * Reset period.
	 * 
	 * @return the string
	 */
	public String resetPeriod(){
	    return getMessage("resetPeriod");
	}
	
	/**
	 * Value less zero.
	 * 
	 * @return the string
	 */
	public String valueLessZero(){
	    return getMessage("valueLessZero");
	}
	
	/**
	 * Time required.
	 * 
	 * @return the string
	 */
	public String timeRequired(){
	    return getMessage("timeRequired");
	}
	
	/**
	 * Minutes.
	 * 
	 * @return the string
	 */
	public String minutes(){
	    return getMessage("minutes");
	}
}

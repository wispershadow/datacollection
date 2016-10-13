var omsdashboardapp = angular.module('omsdashboardapp', []);

omsdashboardapp.factory('dashboardService', ['$http', function($http){
	var workflowactionmappings = {};
	var workflowstatistics = {};
	var workflownodestatistics = {};
	var serviceobj = {
		init: function (){
			$http.get('../omsdashboard/workflowactions').success(function(data, status) {
				workflowactionmappings = data;
			}).error(function(data, status) {
				workflowactionmappings = {};
			});
		},
		
		loadWorkflowStatistics: function(workflowName, startTime, endTime) {
			var fullUrl = '../omsdashboard/searchworkflow?workflowName='+ workflowName + '&startTime=' + startTime + '&endTime=' + endTime;
			$http.get(fullUrl).success(function(data, status) {
				workflowstatistics = data;
			}).error(function(data, status){
				workflowstatistics = {};
			});
		},
		
		loadWorkflowNodeStatistics: function(nodeActionName, startTime, endTime, statInterval, statUnit, topN) {
			var fullUrl = '../omsdashboard/searchworkflownode?nodeActionName='+ nodeActionName + '&startTime=' + startTime + '&endTime=' + endTime + '&statInterval=' + statInterval + '&statUnit=' + statUnit + '&topN=' + topN;
			$http.get(fullUrl).success(function(data, status) {
				workflownodestatistics = data;
			}).error(function(data, status){
				workflownodestatistics = {};
			});
		},
		
		getWorkflowActionMappings: function() {
			return workflowactionmappings;
		},
		
		getWorkflowStatistics: function() {
			return workflowstatistics;
		},
		
		getWorkflowNodeStatistics: function() {
			return workflownodestatistics;
		}
	};
	return serviceobj;
}]
);


omsdashboardapp.factory('dateService', function(){
	var serviceobj = {
		dateFromString: function (str) {
			var m = str.match(/(\d+)\/(\d+)\/(\d+)\s+(\d+):(\d+):(\d+)/);
			return new Date(+m[1], +m[2] - 1, +m[3], +m[4], +m[5], +m[6]);
		},
		parseDate: function(dt) {
			var year = dt.getFullYear() 
			var month = dt.getMonth() + 1;
			var day = dt.getDate();
			var hour = dt.getHours();
			var minute = dt.getMinutes();
			var second = dt.getSeconds();
			return year + '/' + (month>=10?month:('0'+month)) + '/' + (day>=10?day:('0'+day)) + ' ' + (hour>=10?hour:('0'+hour)) + ':' + (minute>=10?minute:('0'+minute)) + ':' + (second>=10?second:('0'+second));
		}
	} 
	return serviceobj;
});	


omsdashboardapp.directive('jqdatepicker', function($parse) {
	return {
		restrict: 'A',
		require: 'ngModel',
		link: function(scope, element, attrs, controller) {
			var ngModel = $parse(attrs.ngModel);
			var updateModel = function(dateText){
				scope.$apply(function(scope){
					ngModel.assign(scope, dateText);
				});
			};	
			
			$(element).datepicker({dateFormat: 'yy/mm/dd', onSelect:updateModel});
			
		}
	};
});

omsdashboardapp.directive('barchart', function($parse) {
	return {
		replace:false,
		restrict:'A',
		scope: {
			chartTitle:'@',
			seriesName:'@',
			chartData:'='
		},
		link: function(scope, element, attrs) {
			var curElem = element;
			scope.$watch('chartData', function(val){
				var chartInd = $(curElem).data('highchartsChart');
				var chart = Highcharts.charts[chartInd];
				chart.series[0].setData(val, true);				
			});
			
			var chartoptions = {
				chart: {
					plotBackgroundColor: null,
					plotBorderWidth: null,
					plotShadow: false,
					type: 'pie'
				},
				title: {
					text: scope.chartTitle
				},
				tooltip: {
					pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
				},
				plotOptions: {
					pie: {
						allowPointSelect: true,
						cursor: 'pointer',
						dataLabels: {
							enabled: true,
							format: '<b>{point.name}</b>: {point.percentage:.1f}%  <br/>count: {point.y}<br/>max: {point.max:.1f}<br/>average: {point.average:.1f}',
							style: {
								color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
							}
						}
					}
				},
				series: [{
					name: scope.seriesName,
					colorByPoint: true,
					data: []
				}]
			};		
			for(var i=0; i<scope.chartData.length; i++) {
				chartoptions.series[0].data.push(scope.chartData[i]);
			}
			
			Highcharts.chart(element.attr("id"), chartoptions);
		}	
	};
});



omsdashboardapp.directive('histogram', function($parse) {
	return {
		replace:false,
		restrict:'A',
		scope: {
			chartTitle:'@',
			seriesName:'@',
			xTitle:'@',
			yTitle:'@',
			chartData:'=',
			categories:'='
		},
		link: function(scope, element, attrs) {
			var curElem = element;
			scope.$watch('chartData', function(val){
				var chartInd = $(curElem).data('highchartsChart');
				var chart = Highcharts.charts[chartInd];
				chart.xAxis[0].categories = scope.categories;
				chart.series[0].setData(val, true);
			});
			
			var chartoptions = {
				chart: {
					type: 'column'
				},
				title: {
					text: scope.chartTitle
				},
				tooltip: {
					pointFormat: 'Average: <b>{point.y:.1f}</b>'
				},
				xAxis: {
					categories: scope.categories,
					title: {
						text: scope.xTitle
					}
				},
				yAxis: {
					min: 0,
					title: {
						text: scope.yTitle
					}
				},
				plotOptions: {
					column: {
						pointPadding: 0.2,
						borderWidth: 0
					}
				},
				series: [{
					name: scope.seriesName,
					colorByPoint: true,
					data: []
				}]
			};		
			for(var i=0; i<scope.chartData.length; i++) {
				chartoptions.series[0].data.push(scope.chartData[i]);
			}
			
			Highcharts.chart(element.attr("id"), chartoptions);
		}		
	};	
});		


omsdashboardapp.directive('topslowlist', function($parse) {
	return {
		replace:false,
		restrict:'A',
		scope: {
			listData:'='
		},
		templateUrl:'../html/topslowlist.html',
		link: function(scope, element, attrs) {
			scope.$watch('listData', function(val){
			});	
		}	
	};	
});



omsdashboardapp.controller('OMSDashboardController', ['$scope', 'dashboardService', 'dateService', function($scope, dashboardService, dateService) {
	$scope.piedataWorkflowStatus = new Array();
	$scope.piedataActionStatus = new Array();
	$scope.histodataExecTime = new Array();
	$scope.histoTimeLine = new Array();
	
	$scope.workflowchanged = function(curworkflow) {
		$scope.actions = $scope.workflowactionmappings[curworkflow];
	};	
	
	$scope.initworkflowactions = function() {
		dashboardService.init();
	};
	
	$scope.searchWorkflow = function() {
		dashboardService.loadWorkflowStatistics($scope.workflow, $scope.startDate + ' ' + $scope.startTime, $scope.endDate + ' ' + $scope.endTime);	
	}
	
	$scope.searchAction = function() {
		dashboardService.loadWorkflowNodeStatistics($scope.action, $scope.startDate + ' ' + $scope.startTime, $scope.endDate + ' ' + $scope.endTime, $scope.statInterval, $scope.statUnit, $scope.topN);
	};
	
	$scope.populateMetricsForChart = function(metrics) {
		var result = new Array();
		for(nextKey in metrics) {
			nextMetricsData = metrics[nextKey];
			var max = nextMetricsData.max;
			if (!jQuery.isNumeric(max)) {
				max = 'NA';
			}
			var average = nextMetricsData.average;
			if (!jQuery.isNumeric(average)) {
				average = 'NA';
			}
			result.push({name:nextKey,y:nextMetricsData.count,max:max,average,average});
		}	
		return result;
	};
	
	$scope.$watch(
		function() {
			return dashboardService.getWorkflowActionMappings();
		},
		
		function(newVal, oldVal) {
			if (newVal != oldVal) {
				$scope.workflowactionmappings = newVal;
				var workflows = new Array();
				for(nextwfname in newVal) {
					workflows.push(nextwfname);
				}
				$scope.workflows = workflows;
				
			}
		}
	);
	
	$scope.$watch(
		function() {
			return dashboardService.getWorkflowNodeStatistics();
		},	
		
		function(newVal, oldVal) {
			if (newVal != oldVal) {
				$scope.workflownodestatistics = newVal;
				var metricsByTimeline = newVal.metricsByTimeline;
				if (metricsByTimeline) {
					var ind = 0;
					var firstKey, lastKey;
					for(var nextKey in metricsByTimeline) {
						if (ind == 0) {
							firstKey = nextKey;
						}
						lastKey = nextKey;
						ind++;
					}
					if (!firstKey) {
						return;
					}
					var startTime = dateService.dateFromString(firstKey);
					var endTime = dateService.dateFromString(lastKey);
					var curTimelineVals = new Array();
					var curTimelineExecTimes = new Array();
					var toAdd;
					if ($scope.statUnit == 'd') {
						toAdd = parseInt($scope.statInterval) * 1000 * 60 * 60 * 24; 
					}
					else if ($scope.statUnit == 'h') {
						toAdd = parseInt($scope.statInterval) * 1000 * 60 * 60; 
					}
					else if ($scope.statUnit == 'm') {
						toAdd = parseInt($scope.statInterval) * 1000 * 60;
					}
					var curTimeObj = new Date();
					for(var curTime = startTime.getTime(); curTime <= endTime.getTime(); curTime = curTime + toAdd) {
						curTimeObj.setTime(curTime);
						var curTimeStr = dateService.parseDate(curTimeObj);
						curTimelineVals.push(curTimeStr);
						var curMetricsData = metricsByTimeline[curTimeStr];
						if (curMetricsData) {
							curTimelineExecTimes.push(curMetricsData.average);
						}
						else {
							curTimelineExecTimes.push(0);
						}
					}
					$scope.histoTimeLine = curTimelineVals;
					$scope.histodataExecTime = curTimelineExecTimes;
				}
			}
		}
	);
	
	$scope.$watch(
		function() {
			return dashboardService.getWorkflowStatistics();
		},	
		
		function(newVal, oldVal) {
			if (newVal != oldVal) {
				$scope.workflowstatistics = newVal;
				var metricsByStatus = newVal.metricsByStatus;
				if (metricsByStatus) {
					$scope.piedataWorkflowStatus = 	$scope.populateMetricsForChart(metricsByStatus);
				}
				var metricsByAction = newVal.metricsByAction;
				if (metricsByAction) {
					$scope.piedataActionStatus = $scope.populateMetricsForChart(metricsByAction);
				}
			}
		}
	);
}]
);
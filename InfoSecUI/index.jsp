<!DOCTYPE html>
<html>
<head>
<!-- Author Neeraj -->
<script src= "jquery-2.1.3.min.js"></script>
<script src="jquery.highlight-5.js"></script>
<script src= "angular.min.js"></script>
<link rel="stylesheet" href="bootstrap.min.css">
<link rel="stylesheet" href="bootstrap-theme.min.css">
<script src="bootstrap.min.js"></script>
<script src="ui-bootstrap-tpls-0.12.1.min.js"></script>
 <script src="underscore-1.2.3.js"></script>
        <script src="underscore.string-2.0.0.js"></script>
        <script src="porter-stemmer.js"></script>
        <script src="sum.js"></script>

<title>Information Security Search Engine</title>
<style>
.url { font-size:12px; color:#006621; }
.highlight { font-weight:bold; }
.title{ font-weight: normal;}
</style>


<script>
var app = angular.module('myApp', ['ui.bootstrap']);

app.controller('customersCtrl', function($scope, $http) {

    $scope.totalItems = 0;
    $scope.currentPage = 1;
    $scope.maxSize = 10;
    $scope.bigTotalItems = 500;
    $scope.bigCurrentPage = 1;
    $scope.getSearch = function(page) {
		
            
			var query = $scope.queryText;
            $scope.bigCurrentPage = page;
            $('.hideOnStart').css("visibility", "visible");
            $http.get("http://localhost:8983/solr/select/?q=" + query + "&wt=json&start=" + (page * 10))
                .success(function(response) {

                    $scope.names = response;

                    for (x in $scope.names.response.docs) {
                        $scope.names.response.docs[x].summary = $scope.getSummary($scope.names.response.docs[x].body);
						if($scope.names.response.docs[x].lastmodified=="null")
							{$scope.names.response.docs[x].lastmodified="";}
						else
							{$scope.names.response.docs[x].lastmodified="Last modified - " + $scope.names.response.docs[x].lastmodified;}//.substring(5, 16);}
                    
					if($scope.names.response.docs[x].summary=='')
						$scope.names.response.docs[x].summary=$scope.names.response.docs[x].meta;
					}

                    setTimeout(function() {
                        $('#SearchResults').removeHighlight();
                        var str = query;
                        var res = str.split(" ");
                        for (i = 0; i < res.length; i++) {
                            $('#SearchResults').highlight(res[i]);
                        }


                    }, 10);

                });
				

        },
		
        $scope.pageChanged = function() {
            $scope.getSearch($scope.bigCurrentPage);
            $log.log('Page changed to: ' + $scope.bigCurrentPage);
        },

		$scope.getSummary = function(textBody) {
			var keywords = $scope.queryText.split(" ");
			
			 var abstract = new sum({ 'corpus': textBody,
			
			//'nSentences': 1,
			'nWords': 50,
			'emphasise': keywords
		  });
		  
		  return abstract.summary;
		},
		
		
        $scope.getSummary1 = function(textBody) {
            var regexSentence = new RegExp("\'(?!(s|t|re|m)( |$))|\\.$|\\. |\\.{2,}|©|`|~|!|@|#|\\$|%|\\^|\\*|\\(|\\)|(^|[^\\w])-+|-+($|[^\\w])|_|=|\\+|\\[|\\]|\\{|\\}|<|>|\\\\|\\||/|;|:|\"|•|–|,|\\?|×|！|·|…|—|（|）|、|：|；|‘|’|“|”|《|》|，|。|？");
            var sentanceArray = textBody.split(regexSentence);
            var len = sentanceArray.length;
            var summaryTag = "";
            var tagCount = 0;
            for (i = 0; i < sentanceArray.length; i++) {
                try {
                    var keywords = $scope.queryText.split(" ");
                    for (j = 0; j < keywords.length; j++) {
                        var res = sentanceArray[i].match(keywords[j]);
                        try {
                            if (res.length > 0 && tagCount < 4) {
								if(sentanceArray[i].length>50)
								{
								 sentanceArray[i] = sentanceArray[i].split(" ", 20).join(" ");
								 }
                                summaryTag += sentanceArray[i] + "... ";
                                tagCount++;
                            } else if (tagCount == 3)
                                return summaryTag;


                        } catch (e) {}
                    }
                } catch (e) {}
            }
            return summaryTag;
        };

});
</script>

</head>
<body>

<div style="width:100%; height:70px; background-color:#F1F1F1; position:absolute; "><div style="background-image:url(infosec-logo.png); background-repeat:no-repeat; width:100px;height:100px; margin-top:8px; margin-left:5px;"></div></div>
<div ng-app="myApp" ng-controller="customersCtrl"> 
<div class="row" style="padding-left:40px;">
<form class="form-inline">
<br/>
  <div class="col-lg-6" style="margin-left:20px;">
    <div class="input-group">
	
	  <input type="text" ng-model="queryText" class="form-control" style="width:500px;" placeholder="Search for...">
      <span class="input-group-btn">
        <button class="btn btn-default" type="submit" ng-click="getSearch(1);">Search</button>
      </span>
    </div><!-- /input-group -->
  </div><!-- /.col-lg-6 -->
  </form>
  </div>
<br/>
<div class="hideOnStart" style="padding-left:60px;visibility:hidden;" >
<h5>About {{ names.response.numFound }} results ({{names.responseHeader.QTime/1000}} seconds)  </h5>
</div>
<hr>
<div id="SearchResults" style="width:1000px; margin-left:20px;">
<ul>
  <dl ng-repeat="x in names.response.docs">
    <dt class="title"  ><a href="{{ x.url }}" style="color: Blue;" >{{ x.title }} </a></dt>
	<dd>{{ x.summary }}</dd>
	<dd style="color:Gray;font-size:12px;">{{ x.lastmodified }}</dd>
	<dd class="url"><div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; width:400px;"> {{ x.url }}</div></dd>
  </dl>
</ul>

</div>
<div id="pagination" class="hideOnStart"  style="visibility:hidden; padding-left:100px;">
<pagination total-items="bigTotalItems" ng-change="pageChanged()" ng-model="bigCurrentPage" max-size="maxSize" class="pagination-sm" boundary-links="true" rotate="false" num-pages="numPages"></pagination>
</div>
 <div class="hideOnStart" style="width:100%; height:70px; background-color:#F1F1F1; visibility:hidden; padding-top:8px; padding-left:40px; color: Blue; "> 
 <h5>
	<b>InfoSec</b> 
	- Information security search engine (International Institute of Information Technology - Hyderabad) 
 </h5>
 </div>
 </body>
</html>
/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function viewResults(element) {
    var xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = function () 
    {
        if((this.readyState === 4) && (this.status === 200))
        {
            document.getElementById("toInsertYourSurveys").innerHTML = this.responseText;
        } 
    }; 
    
    xhttp.open("GET", "ViewResults?surveyID=" + element.id, true);
    xhttp.send(); 
}

function loadMySurveys() {
    var xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = function () 
    {
        if((this.readyState === 4) && (this.status === 200))
        {
            document.getElementById("toInsertYourSurveys").innerHTML = this.responseText;
        } 
    }; 
    
    xhttp.open("GET", "MySurveys", true);
    xhttp.send();  
}

function inputAnswer(surveyID, questionID, text) {
    var xhttp = new XMLHttpRequest();
xhttp.onreadystatechange = function () 
    {
        if((this.readyState === 4) && (this.status === 200))
        {
            console.log(this.responseText);
        } 
    }; 
    xhttp.open("GET", "InputAnswer?surveyID=" + surveyID + "&questionID=" + questionID + "&content=" + text , true);
    xhttp.send();
    incrementQuestion(surveyID, questionID);
}

function incrementAnswer(surveyID, questionID, answerID) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () 
    {
        if((this.readyState === 4) && (this.status === 200))
        {
            console.log(this.responseText);
        } 
    }; 
    xhttp.open("GET", "IncrementAnswer?surveyID=" + surveyID + "&questionID=" + questionID + "&answerID=" + answerID, true);
    xhttp.send();
}

function incrementQuestion(surveyID, questionID) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () 
    {
        if((this.readyState === 4) && (this.status === 200))
        {
            console.log(this.responseText);
        } 
    }; 
    xhttp.open("GET", "IncrementQuestion?surveyID=" + surveyID + "&questionID=" + questionID, true);
    xhttp.send();
}

function incrementSurvey(surveyID) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () 
    {
        if((this.readyState === 4) && (this.status === 200))
        {
            console.log(this.responseText);
        } 
    }; 
    xhttp.open("GET", "IncrementSurvey?surveyID=" + surveyID, true);
    xhttp.send();
}

function finishAnswering() {
    var urlParams = new URLSearchParams(window.location.search);
    var surveyID = urlParams.get('surveyID');
    var questionList = document.getElementById("toInsertSurvey").querySelectorAll("div.questionTile");
    
    incrementSurvey(surveyID);
    console.log("XD");
    for(var i = 0; i < questionList.length; i++)
    {
        if((questionList[i].id)[(questionList[i].id).length - 1] === 't')
        {
            inputAnswer(surveyID, (i + 1), document.getElementById(questionList[i].id).querySelector("textarea.questionText").value);
//            console.log(document.getElementById(questionList[i].id).querySelector("textarea.questionText").value);
        }
        else 
        {
            var answerList = document.getElementById(questionList[i].id).querySelectorAll("input[name='" + questionList[i].id + "']");
            if(answerList.length > 0)
            {
                incrementQuestion(surveyID, (i + 1));
                for(var j = 0; j < answerList.length; j++)
                {
                    if(document.getElementById(answerList[j].id).checked)
                    {
                        console.log(answerList[j].id);
                        incrementAnswer(surveyID, (i + 1), (j + 1));
                    }
                }
            } 
        }
    }
    window.location.href = "index.html";
}

var questionsAdded = 1;
var totalAnswers = new Array(0);
var answersAdded = new Array(0); // to be array of arrays 

function submitAnswers(questionID, answerContent, position, surveyID) {
    var xhttp = new XMLHttpRequest()
    
    xhttp.onreadystatechange = function () 
    {
        if((this.readyState === 4) && (this.status === 200))
        {
            console.log(this.responseText);
        } 
    }; 
    
    xhttp.open("GET", "SubmitAnswer?answerContent=" + answerContent + "&questionID=" + questionID + "&position=" + position + "&surveyID=" + surveyID, true);
    xhttp.send();
}

function submitQuestions(position, surveyID, type) {
    var xhttp = new XMLHttpRequest();

    xhttp.onreadystatechange = function () 
    {
        if((this.readyState === 4) && (this.status === 200))
        {
            if((type === 'r') || (type === 'c'))
            {
                for(var i = 1; i < answersAdded[position].length; i++)
                { 
                    submitAnswers(this.responseText, document.getElementById(answersAdded[position][i] + 't').value, i, surveyID);
                }
                console.log(answersAdded[position]);
            }
            
            if(type === 'y')
            {
                submitAnswers(this.responseText, 'y/n', i, surveyID);
            }
        }
    }; 
    
    xhttp.open("GET", "SubmitQuestion?questionContent=" + document.getElementById('questionText' + answersAdded[position][0]).value +
                "&surveyID=" + surveyID +
                "&type=" + type +
                "&obligatory=" + document.getElementById(answersAdded[position][0] + 'togBtn').checked.toString() +
                "&position=" + (parseInt(position, 10) + 1), true);
    xhttp.send();
}

var operatingSurveyID = null;

function submitSurvey() {
    var xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = function () 
    {
        if((this.readyState === 4) && (this.status === 200))
        {
            for(var i = 0; i < answersAdded.length; i++)
            {
                submitQuestions(i, this.responseText, answersAdded[i][0][answersAdded[i][0].length - 1]);
                console.log(answersAdded[i][0][answersAdded[i][0].length - 1]);
            }
            
            operatingSurveyID = parseInt(this.responseText, 10);
        }
    };
    
    var urlParams = new URLSearchParams(window.location.search); 
    
    xhttp.open("GET", "SubmitSurvey?title=" + urlParams.get('title') 
            + '&description=' + urlParams.get('description') 
            + '&public=' + urlParams.get('isPublic'), true);
    xhttp.send();
}

function loadPreview() {
   
    if (operatingSurveyID !== null) 
    {
        window.location.href = "LoadSurvey.html?surveyID=" + operatingSurveyID + "&preview=" + 'true';
    } 
    else 
    {
        setTimeout(loadPreview, 1000); // try again in 300 milliseconds
    }
}

function loadSurvey() {
    var xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = function () 
    {
        if((this.readyState === 4) && (this.status === 200))
        {
            document.getElementById("toInsertSurvey").innerHTML = this.responseText;
        }
    };
    
    var urlParams = new URLSearchParams(window.location.search); 
    
    xhttp.open("GET", "LoadSurvey?surveyID=" + urlParams.get('surveyID') 
            + "&preview=" + urlParams.get('preview'), true);
    xhttp.send();
}

function addQuestion(type) {
  var div = document.createElement('div');
  div.id = 'q' + questionsAdded + type;
  answersAdded.push(new Array(1));
  answersAdded[answersAdded.length - 1][0] = 'q' + questionsAdded + type;
  totalAnswers.push(1);
  div.className = 'questionDiv';
  
  var beginToh2 = '<div style=\'width: 100%;\'>' +
                        '<h1 id="' + div.id + 'header"' + ' style="float: left; display: inline; text-align: left; color: #02DAAC; font-family: \'Open sans condensed\',' + 
                            'sans-serif; font-size: 40px; padding-left: 5px; padding-top: 7px; padding-bottom: 17px;">Question ' + questionsAdded + '</h1>' +
                        '<div class=\'questionOptions\' onClick="deleteQuestion(\'' + div.id + '\')">Delete question</div>' +
                    '</div>' +
                    '<h2 class="typeText">';
  
  var h2ToEndOfText = '</h2>' +
                    '<textarea id=\'questionText' + div.id + '\' class="questionText"';
            
  var plusStyleMargin = 'maxlength=\'150\' oninput="autoresize(this)" placeholder="Your question here..."></textarea>';
  
  var questionToggleDiv = '<div style="margin-top: 20px; margin-left: 12px;"><div class="typeText" style="font-size: 30px; float: left; margin: 0px;">Answer obligatory?</div>' +
                            '<div style="float right; margin-right: 180px; padding-top: 4px;">' +
                              '<label class="switchQuestion" style="">' +
                                  '<input type="checkbox" id="' + div.id + 'togBtn">' + 
                                  '<div class="sliderQuestion"></div>' +
                              '</label>' +
                            '</div>' +
                          '</div>';
  
  switch(type)
  {
      case 'r':
            div.innerHTML = beginToh2 + 'radio button' + h2ToEndOfText + plusStyleMargin + '<div id=\'' + div.id + 'answers' + '\'>' +
                    '</div>' +
                    '<div id=\'' + div.id + 'add' + '\' style=\'display: flex; margin-top: 20px;\'>' +
                        '<div class=\'dot\' style=\'margin-top: 12px;\' onClick="addAnswer(\'' + div.id + 'answers' + '\')">+</div>' +
                        '<div style="color: #02DAAC; font-family: \'Open sans condensed\', sans-serif; font-size: 30px; padding-left: 20px; padding-top: 7px;">Add answer</div>' +
                    '</div>';
          break;
      
      case 'y':
            div.innerHTML = beginToh2 + 'yes/no' + h2ToEndOfText + ' style="margin-bottom: 50px;" ' + plusStyleMargin +
                    '<h1 class="typeText">This question will let intervieewes answer only "Yes" or "No"</h1>';
          break;
          
      case 'c':
            div.innerHTML = beginToh2 + 'checkbox' + h2ToEndOfText + plusStyleMargin + '<div id=\'' + div.id + 'answers' + '\'>' +
                    '</div>' +
                    '<div id=\'' + div.id + 'add' + '\' style=\'display: flex; margin-top: 20px;\'>' +
                        '<div class=\'dot\' style=\'margin-top: 12px; border-radius: 5px;\' onClick="addAnswer(\'' + div.id + 'answers' + '\')">+</div>' +
                        '<div style="color: #02DAAC; font-family: \'Open sans condensed\', sans-serif; font-size: 30px; padding-left: 20px; padding-top: 7px;">Add answer</div>' +
                    '</div>';
          break;
          
      case 't':
            div.innerHTML = beginToh2 + 'text answer' + h2ToEndOfText + ' style="margin-bottom: 50px;" ' + plusStyleMargin +
                    '<h1 class="typeText">This question will let intervieewes give an individual answer</h1>';
          break;
  }
  
  div.innerHTML += questionToggleDiv;
  
  document.getElementById('questions').appendChild(div);
  questionsAdded++;
  updateHeaders();
}

function addAnswer(gotDiv) {
    var div = document.createElement(gotDiv);
    var questionNumberAndType = gotDiv.replace('answers', '');
    var index = getIndex(answersAdded, questionNumberAndType);
    var additionalStyle = "";

    div.id = questionNumberAndType + 'a' + totalAnswers[index];
    totalAnswers[index]++;
    answersAdded[index].push(div.id);
  //  console.log('Added answer:' + div.id);
    div.className = 'answerDiv';

    if(questionNumberAndType[questionNumberAndType.length - 1] === 'c')
    {
        additionalStyle = 'style="border-radius: 5px;"';
    }

    div.innerHTML = '<div class=\'dot\'' + additionalStyle + 'onClick="deleteAnswer(\'' + 
            div.id + '\')">-</div><div style="width: 89%"><textarea id="' + div.id + 't' +
            '\" class=\"answerText\" style=\'margin-left: 20px;\'maxlength=\'150\' oninput="autoresize(this)" placeholder="Answer here..."></textarea>' + 
            '</div>';

    document.getElementById(gotDiv).appendChild(div);
}

function getIndex(array, value) {
    var i = 0;
    
    while(array[i][0] !== value)
    {
        console.log(array[i][0]);
        i++;
    }
    
    return i;
}

function deleteQuestion(div) {
    var index = getIndex(answersAdded, div);
    
    answersAdded.splice(index, 1);
    totalAnswers.splice(index, 1);
    
    deleteDiv(div);
    updateHeaders();
}

function deleteAnswer(div) {
    var questionNumber = getQuestionNumber(div);
    var index = getIndex(answersAdded, questionNumber);
    
    console.log(answersAdded[index]);
    answersAdded[index].splice(answersAdded[index].indexOf(div), 1);
    
    console.log(answersAdded[index]);
    
    deleteDiv(div);
}

function deleteDiv(div) {
  var myobj = document.getElementById(div);
  myobj.remove();
}

function getQuestionNumber(answerNumber) {
    
    var ret = [];
    var i = 0;
    
    while(answerNumber[i] !== 'r' && 
          answerNumber[i] !== 'y' &&
          answerNumber[i] !== 'c' &&
          answerNumber[i] !== 't')
    {
        ret.push(answerNumber[i]);
        i++;
        console.log(ret);
    }
    
    ret.push(answerNumber[i]);
    
    ret = ret.join("");
    console.log(ret);
    
    return ret;
}

function updateHeaders() {
    
    for(var i = 1; i < answersAdded.length + 1; i++)
    {
        document.getElementById(answersAdded[i - 1][0] + 'header').innerHTML = 'Question ' + i;
    }
}

var resizingTextareas = [].slice.call(document.querySelectorAll('textarea[autoresize]'));

    resizingTextareas.forEach(function(textarea) {
    textarea.addEventListener('input', autoresize, false);
});

function autoresize(textarea) {
    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight+'px';
    textarea.scrollTop = textarea.scrollHeight;
  //  window.scrollTo(window.scrollLeft,(thtextareais.scrollTop+textarea.scrollHeight));
}

var timeFunction = function() {
  var now = new Date(); 
    
  var time = now.getHours() + ':';
  
  if(now.getMinutes() < 10)
  {
      time += '0' + now.getMinutes();
  }
  else
  {
      time += now.getMinutes();
  }
    
    now.setDate(now.getDate() + 7);
   
    date = [now.getDate(),
      now.getMonth()+1,
      now.getFullYear()
    ].join('/');


  element = document.getElementById('time');
  
  if(element !== null)
  {
      element.innerHTML = [date, time].join('   ');
  }
};
setInterval(timeFunction, 1000);


function surveyNextStep()
{
    let title = document.getElementById('surveyTitle').value;
    let description = document.getElementById('surveyDescription').value;
    let public = document.getElementById('publicTogBtn').checked;

    window.location.href = "SurveyCreationSecond.html?title=" + title + "&description=" + description + "&isPublic=" + public.toString();
}

function initNextStep() {

    var urlParams = new URLSearchParams(window.location.search); 

    document.getElementById('surveyTitleDiv').innerHTML = urlParams.get('title').toString();
    document.getElementById('surveyDescriptionDiv').innerHTML = urlParams.get('description');        
    if(urlParams.get('isPublic') === 'true')
    {
        document.getElementById('surveyPublicBoolDiv').innerHTML = 'a public survey';
    }
    else
    {
        document.getElementById('surveyPublicBoolDiv').innerHTML = 'a private survey';
    }
    
    console.log(urlParams.get('title'));
    console.log(urlParams.get('description'));
    console.log(urlParams.get('isPublic'));
}

function process(cont, in2)
{
    var xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = function () 
    {
        if(this.readyState === 4 && this.status === 200)
        {
            document.getElementById(cont).innerHTML = this.responseText;
        }
    };
    
    var xd = document.getElementById(in2).value;
    
    xhttp.open("GET", "test?in2=" + xd, true);
    xhttp.send();
}

function login(nick, password, loginErrorDiv)
{
    var xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = function () 
    {
        if(this.readyState === 4 && this.status === 200)
        {
            if(this.responseText === "ERROR")
            {
                document.getElementById(loginErrorDiv).innerHTML = "<h1 style=\"color: red; font-family: 'Barlow Semi Condensed', sans-serif;\">INCORRECT LOGIN OR PASSWORD</h1>";
            }
            else
            {
                window.location.href = "index.html";
            }
        }
    };
    
    var nickVar = document.getElementById(nick).value;
    var passVar = document.getElementById(password).value;
    
    xhttp.open("POST", "Login?nick=" + nickVar + "&password=" + passVar, true);
    xhttp.send();
}

function register(nick, mail, password, confirmPassword, registerErrorDiv)
{
    var xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = function () 
    {
        if(this.readyState === 4 && this.status === 200)
        {
            var outPrint = this.responseText;
            var outputDiv = "";
            
            if(outPrint[1] === '1')
            {
                outputDiv += "<br\>" + outPrint + "<br\><h1 style=\"color: red;\">USERNAME ALREADY IN USE!</h1>";
            }
            
            if(outPrint[4] === '1')
            {
                outputDiv += "<br\><br\><h1 style=\"color: red;\">CONFIRM PASSWORD INCORRECT!</h1>";
            }
            
            if(outPrint[1] === '0' && outPrint[1] === '0')
            {
                window.location.href = "index.html";
            }
            else
            {
                document.getElementById(registerErrorDiv).innerHTML = outputDiv;
            }
        }
    };
    
    var nickVar = document.getElementById(nick).value;
    var mailVar = document.getElementById(mail).value;
    var passVar = document.getElementById(password).value;
    var cPassVar = document.getElementById(confirmPassword).value;
    
    xhttp.open("POST", "Register?nick=" + nickVar + "&mail=" + mailVar + "&password=" + passVar + "&confirmPassword=" + cPassVar, true);
    xhttp.send();
}

function mainLoad(navBar)
{
    var xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = function () 
    {
        if(this.readyState === 4 && this.status === 200)
        {
            document.getElementById(navBar).innerHTML = this.responseText;
        }
    };
    
    xhttp.open("GET", "MainLoad", true);
    xhttp.send();
}

function logout()
{
    var xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = function () 
    {
        if(this.readyState === 4 && this.status === 200)
        {
            window.location.href = "index.html";
        }
    };
    
    xhttp.open("GET", "Logout", true);
    xhttp.send();
}

function checkLogNavBar(button)
{
    var xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = function () 
    {
        if(this.readyState === 4 && this.status === 200)
        {
            if(this.responseText === "IS_LOGGED_IN")
            {
                if(button === 'ADD')
                {
                    window.location.href = "SurveyCreationFirst.html";
                }
                else
                {
                    window.location.href = "MySurveys.html";
                }
            }
            else
            {
                window.location.href = "LogInPanel.html";
            }
        }
    };
    
    xhttp.open("GET", "CheckLogin", true);
    xhttp.send();
}
grammar MeLa;
// MeLa: Mermaid Language

/* Main application */
app : comment* mission
      comment* coordinator
      (comment* acqMode)*
      comment*
      EOF;


/* Mission parameters */
mission: 'Mission' ':' parkTime missionDepth;
parkTime: 'ParkTime' ':'  NUM 'minutes' ';';
missionDepth: 'ParkDepth' ':'  NUM 'meters' ';';

coordinator: 'Coordinator' ':' descentAcqModes? parkAcqModes? ascentAcqModes? ;
descentAcqModes: 'DescentAcqModes' ':' scheduling ';';
parkAcqModes: 'ParkAcqModes' ':' scheduling ';';
ascentAcqModes: 'AscentAcqModes' ':' scheduling ';';

scheduling: (sequenceSchedule | continuousSchedule)*;
sequenceSchedule: acqModeName 'every' NUM sequenceScheduleTimeUnit;
sequenceScheduleTimeUnit: ('seconds' | 'minutes' | 'hours' | 'days');
continuousSchedule: acqModeName;


/* Acquisition mode */
acqMode: (continuousAcqMode | singleAcqMode) ':'
          comment*
          input
          comment*
          variables
          comment*
          mainsequence
          (comment* subsequence)*
         'endmode;';
continuousAcqMode: 'ContinuousAcqMode' acqModeName;
singleAcqMode: 'ShortAcqMode' acqModeName;


/* Variables */
input: 'Input' ':'
       'sensor' ':' sensorName '(' sensorFrequency ')' ';'
       'data' ':' inputVar '(' inputVarLen ')' ';';
sensorName: WORD;
sensorFrequency: NUM;
inputVar: WORD;
inputVarLen: NUM;

/* Variables */
variables:  'Variables' ':' comment* (variable comment*)*;

variable: varType varName ('(' numParam (',' numParam)* ')')? ';';
varType: WORD;
numParam: NUM | DEC | WORD | EXPONENT;


/* Sequence */
mainsequence: realTimeSequence | processingSequence;
subsequence: realTimeSequence | processingSequence;
realTimeSequence: 'RealTimeSequence' sequenceName ':' (instruction | comment)* 'endseq;';
processingSequence: 'ProcessingSequence' sequenceName ':' (instruction | comment)* 'endseq;';


/* Statement */
instruction: (assignmentInstruction | callInstruction | conditionalInstruction | forLoopInstruction) ';';
assignmentInstruction: varUse '=' data ;
callInstruction: data;
conditionalInstruction: condition;

/* For loop */
forLoopInstruction: 'for' varName ',' varName 'in' varName ':' (instruction | comment)* 'endfor';

/* Condition */
condition: ifbranch 'endif';
ifbranch: 'if' data ':'
        '@probability' '=' probability
        (call | (instruction | comment)*);

probability: nbPerTimeUnit;
nbPerTimeUnit: NUM perTimeUnit;
perTimeUnit: ('per sec' | 'per min' | 'per hour' | 'per day' | 'per week');

call: 'call' sequenceName;


/* Data */
data: varUse | constant | functionCall | operation;
varUse: '!'? varName;
constant: NUM | DEC | STRING | EXPONENT;
functionCall: functionName '(' data? (',' data)* ')';

operation: leftOperand operator rightOperand (operator rightOperand)*;
operator: '<' | '<=' | '>' | '>=' | '==' | '&&' | '||' | '+' | '-' | '*' | '/';
leftOperand: varUse | constant;
rightOperand: varUse | constant;
functionName: WORD;


/* Basics */
acqModeName: WORD;
sequenceName: WORD;
comment: COMMENT;
varName: WORD;

NUM : '-'? [0-9]+;
DEC : '-'? [0-9]+ '.' [0-9]+;
EXPONENT : ('+' | '-')? [0-9]+ '.' [0-9]+ 'e' ('+' | '-')? [0-9]+;
WORD : [a-zA-Z0-9_]+;
STRING: '"' ~[";]+ '"'; // ex: "string"
COMMENT: '/*' '*'* ( ~('*' | '/') | ('*'+ ~[*/]))* '*'* '*/'; // ex: /* comment */

WHITESPACE : [ \t]+ -> skip;
NEWLINES : [\r\n]+ -> skip;


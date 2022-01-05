start = or_expression
		
or_expression
    = head:and_expression tail:(ws "OR"i ws and_expression)*
    {
        let expressions = [head];
        for (var i = 0; i < tail.length; i++) {
            var current = tail[i][3];
            expressions.push(current);
        }   
        
        if (expressions.length == 1){
        	return expressions[0];
        } else {
          return {
              operation: "OR",
              expressions: expressions
          }
        }
    }

and_expression
    =  head:operand tail:(ws "AND"i ws operand)*
    {
        let expressions = [head];
        for (var i = 0; i < tail.length; i++) {
            var current = tail[i][3];
            expressions.push(current);
        }   
       
        if (expressions.length == 1){
        	return expressions[0];
        } else {
          return {
              operation: "AND",
              expressions: expressions
          }
        }
    }

operand
  = "[" _ expr:or_expression _ "]" 
  { 
  		return expr; 
  }
  / comparison_expression

comparison_expression
    = left:(arithmetic_expression) ws op:operator ws right:(arithmetic_expression)
    {
        return {
        	operation: op,
            left: left,
            right: right
        }
    }

arithmetic_expression
  = head:mul_div_expression tail:(ws ("+" / "-") ws mul_div_expression)* 
  {
      	let expressions = [head];
        for (var i = 0; i < tail.length; i++) {
            var current = tail[i][3];
            current.operation = tail[i][1];
            expressions.push(current);
        }  
        
        if (expressions.length == 1) {
        	return expressions[0];
        } else {
            return {
            	type: "expressionGroup",
            	expressions : expressions
            }
        }
  }

mul_div_expression
  = head:power_expression tail:(ws ("*" / "/") ws power_expression)* 
  {
		let expressions = [head];
        for (var i = 0; i < tail.length; i++) {
            var current = tail[i][3];
            current.operation = tail[i][1];
            expressions.push(current);
        }          
        
        if (expressions.length == 1) {
        	return expressions[0];
        } else {
            return {
            	type: "expressionGroup",
            	expressions : expressions
            }
        }
  }

power_expression
  = head:arithmetic_operand tail:(ws ("^") ws arithmetic_operand)* 
  {
		let expressions = [head];
        for (var i = 0; i < tail.length; i++) {
            var current = tail[i][3];
            current.operation = tail[i][1];
            expressions.push(current);
        }          
        
        if (expressions.length == 1) {
        	return expressions[0];
        } else {
            return {
            	type: "expressionGroup",
            	expressions : expressions
            }
        }
  }

arithmetic_operand
  = "[" _ expr:arithmetic_expression _ "]"
  {
  		return expr; 
  }
  / function_chain
  / value
  
operator "comparison_operator"
    = [^ \(\)\"\t\n\ra-zA-z]+
    {
        return text()
    }
    
function_chain
    = head:function tail:("."function)*
    {	
        let functions = [head];
        for (var i = 0; i < tail.length; i++) {
            var current = tail[i][1];
            functions.push(current);
        }      
        return {
        	type: "functionChain",
        	functions:functions
        }
    }

function 
    = name:function_name function_open_bracket _ args:(arguments)* _ function_close_bracket
    {
        return {
            type: "function",
            name: name,
            args: args
        }
    }

function_name "function_name"
    = name:[a-zA-Z1-9]+ 
    {
        return name.join("");
    }

function_open_bracket "function_open_bracket"
    = "("
    {
        return text();
    }

function_close_bracket "function_close_bracket"
    = ")"
    {
        return text();
    }

arguments
    = head:argument tail:(","_ argument)*
    {
        let args = [head];
        for (var i = 0; i < tail.length; i++) {
            var current = tail[i][2];
            args.push(current);
        }      
        return args; 
    }
    
argument "argument"
    = [0-9-mhdWM]+
    {
        return text();
    }
    / [a-zA-Z_0-9]+
    {
        return text();
    }

value "value"
    = [0-9]+((.)?[0-9]+)?
    {
        return {
        	type: "value",
            value: text()
        }
    }

ws "mandatory_space"
    = [ \t\n\r]+
    
_ "optional_space"
    = [ \t\n\r]*
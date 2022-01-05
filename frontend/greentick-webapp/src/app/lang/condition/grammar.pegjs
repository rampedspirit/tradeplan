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
  / filter_name

filter_name "filter_name"
    = name:[a-zA-Z1-9]+ 
    {
        return name.join("");
    }

ws "mandatory_space"
    = [ \t\n\r]+
    
_ "optional_space"
    = [ \t\n\r]*
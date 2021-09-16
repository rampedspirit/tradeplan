import "reflect-metadata";
import { ReferencetimeframeType, CandlenumberType, TimeframeType, SymbolType, NoofcandlesType, DirectionType, OlhcvpriceType, IndexType, RankType, ConditionType, DegreeType, FastperiodType, SlowperiodType, NumberofperiodType, TypeType, NumberofstocksType, PeriodType, MovingaveragetypeType, SignalPeriodType, MultiplierType } from "./filter-language.arg.types";
import { getOperators, Operator, Function, Arg, getFunctions } from "./filter-language.decorators";
import { LFunction, LOperator } from "./filter-language.types";

export class FilterLanguageLibrary {

    @Operator("+", "Add")
    @Operator("-", "Subtract")
    @Operator("*", "Multiply")
    @Operator("/", "Divide")
    @Operator("^", "Power")
    public get arithmeticOperators(): LOperator[] {
        let target = Object.getPrototypeOf(this);
        return getOperators(target, "arithmeticOperators");
    }

    @Operator(">", "Greater Than")
    @Operator(">=", "Greater Than Or Equals")
    @Operator("<", "Less Than")
    @Operator("<=", "Less Than Or Equals")
    @Operator("=", "Equals")
    public get comparisonOperators(): LOperator[] {
        let target = Object.getPrototypeOf(this);
        return getOperators(target, "comparisonOperators");
    }

    @Operator("AND", "AND")
    @Operator("OR", "OR")
    public get logicalOperators(): LOperator[] {
        let target = Object.getPrototypeOf(this);
        return getOperators(target, "logicalOperators");
    }

    public get functions(): LFunction[] {
        let target = Object.getPrototypeOf(this);
        return getFunctions(target);
    }

    @Function("Gives 'Open' value of selected candle", true, true, ["collect", "change", "percentchange", "abs", "ceil", "floor", "round"],
        `   

    Eg: Lets Assume that the current date is 24-Aug-2021, time 12:23 PM. Market started at 9:15 AM and ends at 3:30 PM.
    1) open(m,5m,0)	: Starting point "m" = 12:23 PM, "5m" = In 5min chart, "0" = In the lastest completed 5 min candle, Get the open value, i.e open at 12:15 PM candle.

    2) open(m,5m,-1)	: Starting point "m" = 12:23PM, "5m" = Open 5min chart, "-1" = open value of 2nd completly closed candle BEFORE 12:23PM, i.e Latest closed candle = "0" = 12:15PM 				  candle, one before this candle = "-1" = 12:10 PM candle's open.

    3) open(h,15m,-2)	: open value of 11:30 AM's 15min candle
    4) open(h,15m,0)	: open value of 12:00 PM's 15min candle.
    5) open(d,m,0)	: open value of 9:15 AM's 1min candle
    6) open(W,d,-1)	: open value on 20-Aug-2021 in daily chart
    7) open(M,W,0)	: open value on 2-Aug-2021 in weekly chart
    8) open(h,W,-1)	: open vlaue on 9-Aug-2021 in weekly chart
    9) open(h,1m,5)	: open value of 12:20 PM's 1min candle
    10) open(30m,15m,-1)	: open value of 11:45 AM's 15min candle
    
    `)
    private open(@Arg("START TIME of currently RUNNING/ONGOING/LIVE candle", "m", false) ReferenceTimeFrame: ReferencetimeframeType,
        @Arg("Chart time frame ", "15m", false) TimeFrame: TimeframeType,
        @Arg("Candle number", 0, false) CandleNumber: CandlenumberType) { }


    @Function("Represents the low value of the candle", true, true, ["collect", "change", "percentchange", "abs", "ceil", "floor", "round"],
        `
    Gives "Low" value of selected candle 

    Eg: Lets Assume that the current date is 24-Aug-2021, time 12:23 PM. Market started at 9:15 AM and ends at 3:30 PM.
    
    1) low(m,5m,0)	: Starting point "m" = 12:23 PM, "5m" = open 5min chart, "0" = In the lastest completed 5 min candle, Get the low value, i.e low of 12:15 PM candle.

    2) low(m,5m,-1)	: Starting point "m" = 12:23PM, "5m" = open 5min chart, "-1" = low value of 2nd completly closed candle BEFORE 12:23PM, i.e Latest closed candle = "0" = 12:15PM 				  candle, one before this candle = "-1" = 12:10 PM candle's low 

    3) low(h,15m,-2)	: low value of 11:30 AM's 15min candle
    4) low(h,15m,0)	: low value of 12:00 PM's 15min candle
    5) low(d,m,0)	: low value of 9:15 AM's 1min candle
    6) low(W,d,-1)	: low value on 20-Aug-2021 in daily chart
    7) low(M,W,0)	: low value on 2-Aug-2021 in weekly chart
    8) low(h,W,-1)	: low vlaue on 9-Aug-2021 in weekly chart
    9) low(h,1m,5)	: low value of 12:20 PM's 1min candle
    10) low(30m,15m,-1)	: low value of 11:45 AM's 15min candle  
   
    `)
    private low(@Arg("START TIME of currently RUNNING/ONGOING/LIVE candle", "m", false) ReferenceTimeFrame: ReferencetimeframeType,
        @Arg("Chart time frame ", "15m", false) TimeFrame: TimeframeType,
        @Arg("Candle number", 0, false) CandleNumber: CandlenumberType) { }


    @Function("Represents the high value of the candle", true, true, ["collect", "change", "percentchange", "abs", "ceil", "floor", "round"],
        `
    Gives "High" value of selected candle 

    Eg: Lets Assume that the current date is 24-Aug-2021, time 12:23 PM. Market started at 9:15 AM and ends at 3:30 PM.
    
    1) high(m,5m,0)	: Starting point "m" = 12:23 PM, "5m" = open 5min chart, "0" = In the lastest completed 5 min candle, Get the high value, i.e high of 12:15 PM candle.

    2) high(m,5m,-1)	: Starting point "m" = 12:23PM, "5m" = open 5min chart, "-1" = high value of 2nd completly closed candle BEFORE 12:23PM, i.e Latest closed candle = "0" = 12:15PM 				  candle, one before this candle = "-1" = 12:10 PM candle's high

    3) high(h,15m,-2)	: high value of 11:30 AM's 15min candle
    4) high(h,15m,0)	: high value of 12:00 PM's 15min candle
    5) high(d,m,0)	: high value of 9:15 AM's 1min candle
    6) high(W,d,-1)	: high value on 20-Aug-2021 in daily chart
    7) high(M,W,0)	: high value on 2-Aug-2021 in weekly chart
    8) high(h,W,-1)	: high vlaue on 9-Aug-2021 in weekly chart
    9) high(h,1m,5)	: high value of 12:20 PM's 1min candle
    10) high(30m,15m,-1)	: high value of 11:45 AM's 15min candle
    
    
    `)
    private high(@Arg("START TIME of currently RUNNING/ONGOING/LIVE candle", "m", false) ReferenceTimeFrame: ReferencetimeframeType,
        @Arg("Chart time frame ", "15m", false) TimeFrame: TimeframeType,
        @Arg("Candle number", 0, false) CandleNumber: CandlenumberType) { }


    @Function("Represents the close value of the candle", true, true, ["collect", "change", "percentchange", "abs", "ceil", "floor", "round"],
        `
    Gives "Close" value of selected candle 

    Lets Assume that the current date is 24-Aug-2021, time 12:23 PM. Market started at 9:15 AM and ends at 3:30 PM

    Eg: Lets Assume that the current date is 24-Aug-2021, time 12:23 PM. Market started at 9:15 AM and ends at 3:30 PM.

    1) close(m,5m,0)	: Starting point "m" = 12:23PM, "5m" = Open 5min chart, "0" = Get the Latest closed in 5min chart, i.e Close of 12:15 PM candle.

    2) close(m,5m,-1)	: Starting point "m" = 12:23PM, Timeframe/chart = 5m, "-1" = 2nd completly closed candle BEFORE 12:23PM, i.e Latest closed candle = "0" = 12:15PM candle, one before this 			  candle = "-1" = 12:10 PM candle's close

    3) close(h,15m,-2)	: close value of 11:30 AM's 15min candle
    4) close(h,15m,0)	: close value of 12:00 PM's 15min candle
    5) close(d,m,0)	: close value of 9:15 AM's 1min candle
    6) close(W,d,-1)	: close value on 20-Aug-2021 in daily chart
    7) close(M,W,0)	: close value on 2-Aug-2021 in weekly chart
    8) close(h,W,-1)	: close vlaue on 9-Aug-2021 in weekly chart
    9) close(h,1m,5)	: close value of 12:20 PM's 1min candle
    10) close(30m,15m,-1)	: close value of 11:45 AM's 15min candle)
    
    `
    )
    private close(@Arg("START TIME of currently RUNNING/ONGOING/LIVE candle", "m", false) ReferenceTimeFrame: ReferencetimeframeType,
        @Arg("Chart time frame ", "15m", false) TimeFrame: TimeframeType,
        @Arg("Candle number", 0, false) CandleNumber: CandlenumberType) { }


    @Function("Represents the traded volume of the candle", true, true, ["collect", "change", "percentchange", "abs", "ceil", "floor", "round"],

        `
    Gives "Volume" value of selected candle 

    Lets Assume that the current date is 24-Aug-2021, time 12:23 PM. Market started at 9:15 AM and ends at 3:30 PM

    Note: Volume is always calculated at CLOSE TIME of specified candle. Eg: 12:15 PM has two values in 5 mins chart, close value at 12:10 PM candle and open value at 12:15 PM. If volume at 12:15 PM is requestedm it will give volume of 12:10 PM's volume, but not the 12:15 PM's volume.

    1) volume(m,5m,0)	: Starting point "m" = 12:23PM, "5m" = Open 5min chart, "0" = Get the Latest volume in 5min chart, i.e volume of 12:15 PM 5 min candle

    2) volume(m,5m,-1)	: Starting point "m" = 12:23PM, Timeframe/chart = 5m, "-1" = 2nd completly closed candle BEFORE 12:23PM, i.e Latest closed candle = "0" = 12:15PM candle, one before 			  this candle = "-1" = 12:10 PM candle, So volume of 12:10 PM candle.
    3) volume(h,15m,-2)	: volume of 11:30 AM's 15min candle
    4) volume(h,15m,0)	: volume of 12:00 PM's 15min candle
    5) volume(d,m,0)	: volume of 9:15 AM's 1min candle
    6) volume(W,d,-1)	: volume on 20-Aug-2021 in daily chart
    7) volume(M,W,0)	: volume on 2-Aug-2021 in weekly chart
    8) volume(h,W,-1)	: volume on 9-Aug-2021 in weekly chart
    9) volume(h,1m,5)	: volume of 12:20 PM's 1min candle
    10) volume(30m,15m,-1)	: volume of 11:45 AM's 15min candle
    `)
    private volume(@Arg("START TIME of currently RUNNING/ONGOING/LIVE candle", "m", false) ReferenceTimeFrame: ReferencetimeframeType,
        @Arg("Chart time frame ", "15m", false) TimeFrame: TimeframeType,
        @Arg("Candle number", 0, false) CandleNumber: CandlenumberType) { }


    @Function("Represents the head of the candle", true, true, ["collect", "change", "percentchange", "abs", "ceil", "floor", "round"],
        `
    Head = (High - close)

    Lets Assume that the current date is 24-Aug-2021, time 12:23 PM. Market started at 9:15 AM and ends at 3:30 PM

    1) head(m,5m,0)	: Starting point "m" = 12:23PM, "5m" = Open 5min chart, "0" = Get the Latest head value in 5min chart, i.e head value of 12:15 PM 5 min candle

    2) head(m,5m,-1)	: Starting point "m" = 12:23PM, Timeframe/chart = 5m, "-1" = 2nd completly closed candle BEFORE 12:23PM, i.e Latest closed candle = "0" = 12:15PM candle, one before 			  this candle = "-1" = 12:10 PM candle, So head value of 12:10 PM candle.
    3) head(h,15m,-2)	: head value of 11:30 AM's 15min candle
    4) head(h,15m,0)	: head value of 12:00 PM's 15min candle
    5) head(d,m,0)	: volume of 9:15 AM's 1min candle
    6) head(W,d,-1)	: head value on 20-Aug-2021 in daily chart
    7) head(M,W,0)	: head value on 2-Aug-2021 in weekly chart
    8) head(h,W,-1)	: head value on 9-Aug-2021 in weekly chart
    9) head(h,1m,5)	: head value of 12:20 PM's 1min candle
    10) head(30m,15m,-1)	: head value of 11:45 AM's 15min candle
    `)
    private head(@Arg("START TIME of currently RUNNING/ONGOING/LIVE candle", "m", false) ReferenceTimeFrame: ReferencetimeframeType,
        @Arg("Chart time frame ", "15m", false) TimeFrame: TimeframeType,
        @Arg("Candle number", 0, false) CandleNumber: CandlenumberType) { }


    @Function("Represents the tail of the candle", true, true, ["collect", "change", "percentchange", "abs", "ceil", "floor", "round"],
        `
    tail= (open - low)

    Lets Assume that the current date is 24-Aug-2021, time 12:23 PM. Market started at 9:15 AM and ends at 3:30 PM

    1) tail(m,5m,0)	: Starting point "m" = 12:23PM, "5m" = Open 5min chart, "0" = Get the Latest tail value in 5min chart, i.e tail value of 12:15 PM 5 min candle

    2) tail(m,5m,-1)	: Starting point "m" = 12:23PM, Timeframe/chart = 5m, "-1" = 2nd completly closed candle BEFORE 12:23PM, i.e Latest closed candle = "0" = 12:15PM candle, one before 			  this candle = "-1" = 12:10 PM candle, So tail value of 12:10 PM candle.
    3) tail(h,15m,-2)	: tail value of 11:30 AM's 15min candle
    4) tail(h,15m,0)	: tail value of 12:00 PM's 15min candle
    5) tail(d,m,0)	: tail value of 9:15 AM's 1min candle
    6) tail(W,d,-1)	: tail value on 20-Aug-2021 in daily chart
    7) tail(M,W,0)	: tail value on 2-Aug-2021 in weekly chart
    8) tail(h,W,-1)	: tail value on 9-Aug-2021 in weekly chart
    9) tail(h,1m,5)	: tail value of 12:20 PM's 1min candle
    10) tail(30m,15m,-1)	: tail value of 11:45 AM's 15min candle
    `)
    private tail(@Arg("START TIME of currently RUNNING/ONGOING/LIVE candle", "m", false) ReferenceTimeFrame: ReferencetimeframeType,
        @Arg("Chart time frame ", "15m", false) TimeFrame: TimeframeType,
        @Arg("Candle number", 0, false) CandleNumber: CandlenumberType) { }


    @Function("Represents the body of the candle", true, true, ["collect", "change", "percentchange", "abs", "ceil", "floor", "round"],
        `
    body = (close - open). Its positive for Green candle, and negative for Red candle.

    Lets Assume that the current date is 24-Aug-2021, time 12:23 PM. Market started at 9:15 AM and ends at 3:30 PM
    
    1) body(m,5m,0)	: Starting point "m" = 12:23PM, "5m" = Open 5min chart, "0" = Get the Latest body value in 5min chart, i.e body value of 12:15 PM 5 min candle

    2) body(m,5m,-1)	: Starting point "m" = 12:23PM, Timeframe/chart = 5m, "-1" = 2nd completly closed candle BEFORE 12:23PM, i.e Latest closed candle = "0" = 12:15PM candle, one before 			  this candle = "-1" = 12:10 PM candle, So body value of 12:10 PM candle.
    3) body(h,15m,-2)	    : body value of 11:30 AM's 15min candle
    4) body(h,15m,0)	    : body value of 12:00 PM's 15min candle
    5) body(d,m,0)	: body value of 9:15 AM's 1min candle
    6) body(W,d,-1)	: body value on 20-Aug-2021 in daily chart
    7) body(M,W,0)	: body value on 2-Aug-2021 in weekly chart
    8) body(h,W,-1)	: body value on 9-Aug-2021 in weekly chart
    9) body(h,1m,5)	: body value of 12:20 PM's 1min candle
    10) body(30m,15m,-1)	: body value of 11:45 AM's 15min candle
    `)
    private body(@Arg("START TIME of currently RUNNING/ONGOING/LIVE candle", "m", false) ReferenceTimeFrame: ReferencetimeframeType,
        @Arg("Chart time frame ", "15m", false) TimeFrame: TimeframeType,
        @Arg("Candle number", 0, false) CandleNumber: CandlenumberType) { }


    @Function("Represents the shadow of the candle", true, true, ["collect", "change", "percentchange", "abs", "ceil", "floor", "round"],
        `
    shadow = (high - low).

    Lets Assume that the current date is 24-Aug-2021, time 12:23 PM. Market started at 9:15 AM and ends at 3:30 PM

    1) shadow(m,5m,0)	: Starting point "m" = 12:23PM, "5m" = Open 5min chart, "0" = Get the Latest body value in 5min chart, i.e shadow value of 12:15 PM 5 min candle

    2) shadow(m,5m,-1)	: Starting point "m" = 12:23PM, Timeframe/chart = 5m, "-1" = 2nd completly closed candle BEFORE 12:23PM, i.e Latest closed candle = "0" = 12:15PM candle, one before 			  this candle = "-1" = 12:10 PM candle, So shadow value of 12:10 PM candle.
    3) shadow(h,15m,-2)	: shadow value of 11:30 AM's 15min candle
    4) shadow(h,15m,0)	: shadow value of 12:00 PM's 15min candle
    5) shadow(d,m,0)	: shadow value of 9:15 AM's 1min candle
    6) shadow(W,d,-1)	: shadow value on 20-Aug-2021 in daily chart
    7) shadow(M,W,0)	: shadow value on 2-Aug-2021 in weekly chart
    8) shadow(h,W,-1)	: shadow value on 9-Aug-2021 in weekly chart
    9) shadow(h,1m,5)	: shadow value of 12:20 PM's 1min candle
    10) shadow(30m,15m,-1)	: shadow value of 11:45 AM's 15min candle
    `)
    private shadow(@Arg("START TIME of currently RUNNING/ONGOING/LIVE candle", "m", false) ReferenceTimeFrame: ReferencetimeframeType,
        @Arg("Chart time frame ", "15m", false) TimeFrame: TimeframeType,
        @Arg("Candle number", 0, false) CandleNumber: CandlenumberType) { }

    @Function("Represents a candle", true, true, ["macd", "rsi"],
        `
    candle = holds all OLHCV values of a selected candle.

    Lets Assume that the current date is 24-Aug-2021, time 12:23 PM. Market started at 9:15 AM and ends at 3:30 PM

    1) candle(m,5m,0)	: Starting point "m" = 12:23PM, "5m" = Open 5min chart, "0" = Get the Latest OLHCV values in 5min chart, i.e all OLHCV values of 12:15 PM's 5 min candle

    2) candle(m,5m,-1)	: Starting point "m" = 12:23PM, Timeframe/chart = 5m, "-1" = 2nd completly closed candle BEFORE 12:23PM, i.e Latest closed candle = "0" = 12:15PM candle, one before 			  this candle = "-1" = 12:10 PM candle, So OLHCV values of 12:10 PM candle.
    3) candle(h,15m,-2)	: OLHCV values of 11:30 AM's 15min candle
    4) candle(h,15m,0)	: OLHCV values of 12:00 PM's 15min candle
    5) candle(d,m,0)	: OLHCV values of 9:15 AM's 1min candle
    6) candle(W,d,-1)	: OLHCV values on 20-Aug-2021 in daily chart
    7) candle(M,W,0)	: OLHCV values on 2-Aug-2021 in weekly chart
    8) candle(h,W,-1)	: OLHCV values on 9-Aug-2021 in weekly chart
    9) candle(h,1m,+5)	: OLHCV values of 12:20 PM's 1min candle
    10) candle(30m,15m,-1)	: OLHCV values of 11:45 AM's 15min candle 
    `)
    private candle(@Arg("START TIME of currently RUNNING/ONGOING/LIVE candle", "m", false) ReferenceTimeFrame: ReferencetimeframeType,
        @Arg("Chart time frame ", "15m", false) TimeFrame: TimeframeType,
        @Arg("Candle number", 0, false) CandleNumber: CandlenumberType) { }

    @Function("Collects a number values from the defined starting candle upto specified count either in forward or backward direction", false, false, ["get", "max", "min", "sum", "average", "median", "mode", "range", "variance", "stdDev", "sma", "ema", "trendline", "LinearRegline", "valueAt"],
        `
    collect = collects OLHCV data or the indicator values for user specified period.

    Lets Assume that the current date is 24-Aug-2021, time 12:23 PM. Market started at 9:15 AM and ends at 3:30 PM

    Eg: 

    1) open(m,15m,0).collect(100).sma(12) : This means
    a) "open(m,15m,0)" : Starting point "m" = 12:23 PM, "5m" = In 5min chart, "0" = In the lastest completed 5 min candle, Get the open value, i.e open at 12:15 PM candle.
    b) "collect(100)" : From 12:15PM, collect the "open" values of last 100 period is 15 mins chart.
    c) "sma(12)" : For collected 100 open values, calculate the 12 period sma.
    d) "valueAt(0)" : Gives the 12 period sma value at 12:15 PM.

    The above code calculates the 12 period sma of last 100 collected values and gives the latest value.

    2) candle(m,15m,0).macd(12,26,10).collect(100).sma(12).valueAt(0) > 100	: This means
    a) "candle(m,5m,0)" : Starting point "m" = 12:23PM, "5m" = Open 5min chart, "0" = Get the Latest OLHCV values in 5min chart, i.e all OLHCV values of 12:15 PM's 5 min candle
    b) "macd(9,20,10)" : calculate the value of "macd line" for 9 ema and 20 ema period at 12:15 PM's candle.
    c) "collect(100)" : collectes macd line values for last 100 period from 12:15 PM 
    d) "sma(12)" : For collect 100 value in step "c", sma(12) will calculates the 12 period sma.
    e) "valueAt" : Gives the value of 12 period sma of last 100 period macd line at 12:15 PM.

    This code will check if the 12 period sma of last 100 collected macd line values at 12:15 PM is greater than 100 or not.

    `)
    private collect(@Arg("No. of candles", 100, false) NoOfCandles: NoofcandlesType,
        @Arg("Direction to collect the candles", "BACKWARD", true) Direction: DirectionType) { }


    @Function("Calculates the signal line for all the indicators which have signal lines and gives details about crossovers (Bullish/Bearish crossovers, crossover counts)", false, false, ["crossover", "crossoverCount", "valueAt"],

        `
    Note: Stream fucntion can ONLY BE USED for indicator's having signal lines by DEFAULT. All indicator line and indicator's signal line crossover related queries are addressed by stream function.  


    3 options are there for stream: 

    macd indicator has been taken as example to show the usage.

    crossover(0,BULLISH): Checks if the macd's signal line at latest closed candle has "crossed above" the macd line. 
    crossovercount(BULLISH): checks the number of times BULLISH or BEARSIH crossovers has occured between macd line and macd signal line in a given period.
    valueAt(0): Gives the value of macd's signal line at latest completed candle.

    Lets Assume that the current date is 24-Aug-2021, time 12:23 PM. Market started at 9:15 AM and ends at 3:30 PM

    Eg: 

    1) candle(m,15m,0).macd(9,20,10).stream(100).crossover(0,BULLISH) = 1 : This means, 
    a) "candle(m,5m,0)" : Starting point "m" = 12:23PM, "5m" = Open 5min chart, "0" = Get the Latest OLHCV values in 5min chart, i.e all OLHCV values of 12:15 PM's 5 min candle.
    b) "macd(9,20,9)" : calculates the value of "macd line" for 9 ema and 20 ema period at 12:15 PM's candle.
    c) "stream(100)": using macd line, stream will calculate macd signal line for last 100 period. So, this will have 100 macd signal values.
    d) "crossover(0,BULLISH)" : For collected macd line and macd signal line, at 12:15 PM, this will check if macd signal line has made "BULLISH" crossover or not. "1" means crossover has happend and "0" means its not crossed. Similarly, for BEARISH crossover can also be checked.

    This code will check if at latest closed candle, a BULLISH crossover of macd line and macd signal line has occured or not.

    2) candle(m,15m,0).macd(12,26).stream(100).crossoverCount(BULLISH) > 5 : This means, 
    a) "candle(m,5m,0)" : Starting point "m" = 12:23PM, "5m" = Open 5min chart, "0" = Get the Latest OLHCV values in 5min chart, i.e all OLHCV values of 12:15 PM's 5 min candle.
    b) "macd(9,20,9)" : calculates the value of "macd line" for 9 ema and 20 ema period at 12:15 PM's candle.
    c) "stream(100)": using macd line, stream will calculate macd signal line for last 100 period. So, this will have 100 macd signal values.
    d) "crossoverCount(BULLISH)" : This will count the number of times the BULLISH crossover has occured between macd line and macd signal line.

    This code will counts the number of times the macd line and macd signal line has made BULLISH crossovers and check if the count is greater than 5 or not.

    3) candle(m,15m,0).macd(12,26).stream(100).valueAt(0) > 100 : This means, 
    a) "candle(m,5m,0)" : Starting point "m" = 12:23PM, "5m" = Open 5min chart, "0" = Get the Latest OLHCV values in 5min chart, i.e all OLHCV values of 12:15 PM's 5 min candle.
    b) "macd(9,20,9)" : calculates the value of "macd line" for 9 ema and 20 ema period at 12:15 PM's candle.
    c) "stream(100)": using macd line, stream will calculate macd signal line for last 100 period. So, this will have 100 macd signal values.
    d) "valueAt(0)" : Gives the macg signal line value at latest closed candle, i.e at 12:15 PM.

    This code will check if the latest macd signal line value is greater than 100 or not.
    
    `)
    private stream(@Arg("No. of candles", 100, false) NoOfCandles: NoofcandlesType,
        @Arg("Direction to collect the candles", "BACKWARD", true) Direction: DirectionType) { }


    @Function("Represents the percentage change in value of the candle", true, true, ["collect", "abs", "ceil", "floor", "round"])
    private percentchange() { }


    @Function("Represents the change in value of the candle", true, true, ["collect", "abs", "ceil", "floor", "round"])
    private change() { }


    @Function("name of security", true, true, ["collect", "change", "percentchange", "abs", "ceil", "floor", "round"])
    private security(@Arg("START TIME of currently RUNNING/ONGOING/LIVE candle", "m", false) ReferenceTimeFrame: ReferencetimeframeType,
        @Arg("Chart time frame ", "15m", false) TimeFrame: TimeframeType,
        @Arg("Candle number", 0, false) CandleNumber: CandlenumberType,
        @Arg("Name of stock/index", "INFY", false) symbol: SymbolType,
        @Arg("one of the olhcv", "CLOSE", false) olhcvPrice: OlhcvpriceType) { }

    @Function("Gets the entry at the specified index", false, true, ["abs", "ceil", "floor", "round"])
    private valueOf(@Arg("one of the olhcv", "CLOSE", false) olhcvPrice: OlhcvpriceType) { }


    @Function("value at give index", false, true, ["abs", "ceil", "floor", "round"])
    private valueAt(@Arg("index (zero based) from reference", 0, false) index: IndexType) { }


    @Function("return position of interested value", false, true, ["valueOf"])
    private position() { }


    @Function("Gets the entry at the specified index", false, true, ["abs", "ceil", "floor", "round"])
    private get(@Arg("index (zero based) from reference", 0, false) Index: IndexType) { }


    @Function("Maximum among the previously collected values", false, true, ["abs", "ceil", "floor", "round", "position"])
    private max(@Arg("rank of value with respect to maximum as condition.  example max(1) is greates of all values  max(3) is 3rd greatest of all values", 1, false) Rank: RankType) { }


    @Function("Minimum among the previously collected values", false, true, ["abs", "ceil", "floor", "round", "position"])
    private min(@Arg("rank of value with respect to maximum as condition.  example max(1) is greates of all values  max(3) is 3rd greatest of all values", 1, false) Rank: RankType) { }


    @Function("Sum of all the previously collected values", false, true, ["abs", "ceil", "floor", "round"])
    private sum() { }


    @Function("average of all the previously collected values", false, true, ["abs", "ceil", "floor", "round"])
    private average() { }


    @Function("Median of all the previously collected values", false, true, ["abs", "ceil", "floor", "round"])
    private median() { }


    @Function("Mode of all the previously collected values", false, true, ["abs", "ceil", "floor", "round"])
    private mode() { }


    @Function("Range of all the previously collected values", false, true, ["abs", "ceil", "floor", "round"])
    private range() { }


    @Function("Variance of all the previously collected values", false, true, ["abs", "ceil", "floor", "round"])
    private variance() { }


    @Function("Standard Deviation of all the previously collected values", false, true, ["abs", "ceil", "floor", "round"])
    private stdDev() { }


    @Function("touchPointCount", false, true, [])
    private touchPointCount() { }


    @Function("return index of the touch point from reference.", false, true, ["valueOf"])
    private touchPoint(@Arg("index (zero based) from reference", 0, false) index: IndexType) { }


    @Function("trendline", false, false, ["slope", "touchPointCount", "touchPoint", "valueAt"])
    private trendline(@Arg("Direction to collect the candles", "BACKWARD", false) direction: DirectionType,
        @Arg("MSL-> Minimum slope, ZCO -> Zero cross over of points", "MSL", false) condition: ConditionType) { }


    @Function("LinearRegline", false, false, ["slope"])
    private LinearRegline(@Arg("degeree of std deviation", 1, false) degree: DegreeType) { }


    @Function("Slope of all the previously collected values", false, true, ["abs", "ceil", "floor", "round"])
    private slope() { }


    @Function("Absolute of the collected value", false, false, [])
    private abs() { }


    @Function("Ceiling of the collected value", false, false, [])
    private ceil() { }


    @Function("Floor of the collected value", false, false, [])
    private floor() { }


    @Function("Rounding of the collected value", false, false, [])
    private round() { }



    // Start of indicators.

    @Function("Calculates the 'macd line' for given fast ema period and slow ema period.", false, false, ["collect", "stream", "valueAt", "abs", "ceil", "floor", "round"],
        `

    macd : syntax : macd(Fast_ema_period,slow_ema_period,signa_line_ema_period,[OLHCV]) : Calculates the macd line for given fast ema period and slow ema period for 'close' values (by default).
    User can also calcuate macd for any of OLHCV values.

    Note: All indicators should be used after "candle" function.

    3 options are available to work with macd indicator.

    collect(50) : collectes macd line for last 50 period from user specified reference 
    stream(50)  : calculates macd signal line for last 50 period from user specified reference
    valueAt(0)  : Gives value of macd line at user specified reference

    Lets Assume that the current date is 24-Aug-2021, time 12:23 PM. Market started at 9:15 AM and ends at 3:30 PM

    Eg: 

    1) candle(m,5m,0).macd(9,20,10).valueAt(0) > 100 : This means, 
    a) "candle(m,5m,0)" : Starting point "m" = 12:23PM, "5m" = Open 5min chart, "0" = Get the Latest OLHCV values in 5min chart, i.e all OLHCV values of 12:15 PM's 5 min candle
    b) "macd(9,20,10)" : calculates the value of "macd line" for 9 ema and 20 ema period at 12:15 PM's candle.
    c) "valueAt(0) : Gives the value of "macd line" at latest completed 15min candle, i.e at 12:15 PM candle.

    This code check if the macd line's value at 12:15 PM is greater than 100 or not.

    2) candle(m,15m,0).macd(12,26,10).collect(100).sma(12).valueAt(0) > 100
    a) "candle(m,5m,0)" : Starting point "m" = 12:23PM, "5m" = Open 5min chart, "0" = Get the Latest OLHCV values in 5min chart, i.e all OLHCV values of 12:15 PM's 5 min candle
    b) "macd(9,20,10)" : calculate the value of "macd line" for 9 ema and 20 ema period at 12:15 PM's candle.
    c) "collect(100)" : collectes macd line values for last 100 period from 12:15 PM 
    d) "sma(12)" : For collect 100 value in step "c", sma(12) will calculates the 12 period sma.
    e) "valueAt" : Gives the value of 12 period sma of last 100 period macd line at 12:15 PM.

    This code will check if the 12 period sma of last 100 collected macd line values at 12:15 PM is greater than 100 or not.

    3) candle(m,15m,0).macd(9,20,10).stream(100).crossover(0,BULLISH) = 1 : This means, 
    a) "candle(m,5m,0)" : Starting point "m" = 12:23PM, "5m" = Open 5min chart, "0" = Get the Latest OLHCV values in 5min chart, i.e all OLHCV values of 12:15 PM's 5 min candle.
    b) "macd(9,20,9)" : calculates the value of "macd line" for 9 ema and 20 ema period at 12:15 PM's candle.
    c) "stream(100)": using macd line, stream will calculate macd signal line for last 100 period. So, this will have 100 macd signal values.
    d) "crossover(0,BULLISH)" : For collected macd line and macd signal line, at 12:15 PM, this will check if macd signal line has made "BULLISH" crossover or not. "1" means crossover has happend and "0" means its not crossed. Similarly, for BEARISH crossover can also be checked.

    This code will check if at latest closed candle, a BULLISH crossover of macd line and macd signal line has occured or not.

    For more help on stream, please check the "stream" function usage.
    `)
    private macd(@Arg("number of period to use as fast ema for macd", 12, false) fastPeriod: FastperiodType,
        @Arg("number of period to use as slow ema for macd", 26, false) slowPeriod: SlowperiodType,
        @Arg("Signal period", 10, false) signalPeriod: SignalPeriodType,
        @Arg("one of the olhcv for macd calculation, default of this indicator is 'close' value", "CLOSE", true) olhcvPrice: OlhcvpriceType) { }


    @Function("Simple Moving Average of previously collected values", false, false, ["valueAt", "SMAg", "EMAg"])
    private sma(@Arg("number of period to use for simple moving average", 12, false) NumberOfPeriod: NumberofperiodType) { }


    @Function("simple Moving Average as signal", false, false, ["valueAt", "crossover", "crossoverCount", "distanceAt"])
    private SMAg(@Arg("number of period to use for simple moving average", 12, false) NumberOfPeriod: NumberofperiodType) { }


    @Function("exponential Moving Average of previously collected values", false, false, ["valueAt", "SMAg", "EMAg"])
    private ema(@Arg("number of period to use for simple moving average", 12, false) NumberOfPeriod: NumberofperiodType) { }


    @Function("exponential Moving Average as signal", false, false, ["valueAt", "crossover", "crossoverCount", "distanceAt"])
    private EMAg(@Arg("number of period to use for simple moving average", 12, false) NumberOfPeriod: NumberofperiodType) { }


    @Function("return index of the crossover position from reference.", false, true, [])
    private crossover(@Arg("index (zero based) from reference", 0, false) index: IndexType,
        @Arg("type of cross over", "BULLISH", false) type: TypeType) { }


    @Function("return how many times crossover happen.", false, true, [])
    private crossoverCount(@Arg("type of cross over", "BULLISH", false) type: TypeType) { }


    @Function("return distance between baseline and signal line.", false, true, ["abs", "ceil", "floor", "round"])
    private distanceAt(@Arg("index (zero based) from reference", 0, false) index: IndexType) { }


    /* @Function("Advance decline ratio", false, true, ["collect", "abs", "ceil", "floor", "round"])
     private ADR(@Arg("index or watchlist", "NIFTY_50", false) NumberOfStocks: NumberofstocksType) { }
 
 
     @Function("Accummulation /Distribution indicator", false, true, ["collect", "abs", "ceil", "floor", "round"])
     private ADI() { }


    @Function("ArronUp", false, false, ["collect", "stream", "valueAt", "abs", "ceil", "floor", "round"])
    private AroonUp(@Arg("number of period to use for AroonUp", 25, false) period: PeriodType,
        @Arg("one of the olhcv", "CLOSE", false) olhcvPrice: OlhcvpriceType) { }


    @Function("ArronDwon", false, false, ["abs", "ceil", "floor", "round", "valueAt", "average", "crossover", "crossoverCount"])
    private AroonDown(@Arg("number of period to use for AroonUp", 25, false) period: PeriodType,
        @Arg("one of the olhcv", "CLOSE", false) olhcvPrice: OlhcvpriceType) { }


    @Function("True Range", false, true, ["collect", "abs", "ceil", "floor", "round"])
    private TR(@Arg("number of period to use for AroonUp", 25, false) period: PeriodType) { }


    @Function("Average True Range", false, true, ["collect", "abs", "ceil", "floor", "round"])
    private ATR(@Arg("number of period to use for AroonUp", 25, false) period: PeriodType,
        @Arg("period to be considered for calculation of true range", "EMA", false) movingAverageType: MovingaveragetypeType) { }

        @Function("Supertrend indicating trends", false, true, ["collect", "abs", "ceil", "floor", "round"])
    private SUPERTREND(@Arg("number of period to use for Supertrend calculation", 7, false) period: PeriodType,
        @Arg("Multiplier for Supertrend calculation", 3, false) Multiplier: MultiplierType) { }

        */


    @Function("Calculates the rsi value for given period. close values are used as default values for calculating rsi value, but user can calculate rsi of any of OLHCV values.", false, false, ["collect", "valueAt", "abs", "ceil", "floor", "round"],
        `
    rsi : syntax : rsi(rsi_period,[OLHCV]) : Calculates the rsi value for given period. close values are used as default values for calculating rsi value, but user can calculate rsi of any of OLHCV values.

    Note: All indicators should be used after "candle" function.

    2 main options are available to work with rsi indicator.

    collect(50) : collectes rsi value for last 50 period from user specified reference 
    valueAt(0)  : Gives value of rsi at user specified reference

    Lets Assume that the current date is 24-Aug-2021, time 12:23 PM. Market started at 9:15 AM and ends at 3:30 PM

    Eg: 

    1) candle(m,15m,0).rsi(14).collect(100).valueAt(0) > 80 : This means, 
    a) "candle(m,5m,0)" : Starting point "m" = 12:23PM, "5m" = Open 5min chart, "0" = Get the Latest OLHCV values in 5min chart, i.e all OLHCV values of 12:15 PM's 5 min candle
    b) "rsi(14)" : calculates the value of 14 period rsi
    c) "collect(100)" : collects 100 values of 14 period rsi from starting point
    d) "valueAt(0)" : Gives the value of rsi at latest completed 15min candle, i.e at 12:15 PM candle.

    This code check if the rsi value at 12:15 PM is greater than 80 or not.

    2) candle(m,15m,0).rsi(14).collect(100).sma(12).valueAt(0) > candle(m,15m,0).rsi(14).valueAt(0) : This means,
    a) "candle(m,5m,0)" : Starting point "m" = 12:23PM, "5m" = Open 5min chart, "0" = Get the Latest OLHCV values in 5min chart, i.e all OLHCV values of 12:15 PM's 5 min candle
    b) "rsi(14)" : calculates the value of 14 period rsi
    c) "collect(100)" : collects 100 values of 14 period rsi from starting point
    d) "sma(12)" : calculates 12 period sma of collected 100 values of 14 period rsi.
    e) "valueAt(0)" : Gives the value of 12 period sma of 14 period rsi at latest completed 15min candle, i.e at 12:15 PM candle.

    So the left hand side of the code will check if the 12 period sma of 14 period rsi is greater than the 14 period rsi at current position or not.
        
    `)
    private rsi(@Arg("number of period to use for RSI Calculation", 14, false) period: PeriodType,
        @Arg("one of the olhcv for macd calculation, default of this indicator is 'close' value", "CLOSE", true) olhcvPrice: OlhcvpriceType) { }




}
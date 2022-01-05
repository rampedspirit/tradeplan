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

    @Function("Gives 'Open' value of selected candle", true, true, ["collect", "change", "percentchange", "abs", "ceil", "floor", "round"])
    private open(@Arg("START TIME of currently RUNNING/ONGOING/LIVE candle", "m", false) ReferenceTimeFrame: ReferencetimeframeType,
        @Arg("Chart time frame ", "15m", false) TimeFrame: TimeframeType,
        @Arg("Candle number", 0, false) CandleNumber: CandlenumberType) { }


    @Function("Represents the low value of the candle", true, true, ["collect", "change", "percentchange", "abs", "ceil", "floor", "round"])
    private low(@Arg("START TIME of currently RUNNING/ONGOING/LIVE candle", "m", false) ReferenceTimeFrame: ReferencetimeframeType,
        @Arg("Chart time frame ", "15m", false) TimeFrame: TimeframeType,
        @Arg("Candle number", 0, false) CandleNumber: CandlenumberType) { }


    @Function("Represents the high value of the candle", true, true, ["collect", "change", "percentchange", "abs", "ceil", "floor", "round"])
    private high(@Arg("START TIME of currently RUNNING/ONGOING/LIVE candle", "m", false) ReferenceTimeFrame: ReferencetimeframeType,
        @Arg("Chart time frame ", "15m", false) TimeFrame: TimeframeType,
        @Arg("Candle number", 0, false) CandleNumber: CandlenumberType) { }


    @Function("Represents the close value of the candle", true, true, ["collect", "change", "percentchange", "abs", "ceil", "floor", "round"])
    private close(@Arg("START TIME of currently RUNNING/ONGOING/LIVE candle", "m", false) ReferenceTimeFrame: ReferencetimeframeType,
        @Arg("Chart time frame ", "15m", false) TimeFrame: TimeframeType,
        @Arg("Candle number", 0, false) CandleNumber: CandlenumberType) { }


    @Function("Represents the traded volume of the candle", true, true, ["collect", "change", "percentchange", "abs", "ceil", "floor", "round"])
    private volume(@Arg("START TIME of currently RUNNING/ONGOING/LIVE candle", "m", false) ReferenceTimeFrame: ReferencetimeframeType,
        @Arg("Chart time frame ", "15m", false) TimeFrame: TimeframeType,
        @Arg("Candle number", 0, false) CandleNumber: CandlenumberType) { }


    @Function("Represents the head of the candle", true, true, ["collect", "change", "percentchange", "abs", "ceil", "floor", "round"])
    private head(@Arg("START TIME of currently RUNNING/ONGOING/LIVE candle", "m", false) ReferenceTimeFrame: ReferencetimeframeType,
        @Arg("Chart time frame ", "15m", false) TimeFrame: TimeframeType,
        @Arg("Candle number", 0, false) CandleNumber: CandlenumberType) { }


    @Function("Represents the tail of the candle", true, true, ["collect", "change", "percentchange", "abs", "ceil", "floor", "round"])
    private tail(@Arg("START TIME of currently RUNNING/ONGOING/LIVE candle", "m", false) ReferenceTimeFrame: ReferencetimeframeType,
        @Arg("Chart time frame ", "15m", false) TimeFrame: TimeframeType,
        @Arg("Candle number", 0, false) CandleNumber: CandlenumberType) { }


    @Function("Represents the body of the candle", true, true, ["collect", "change", "percentchange", "abs", "ceil", "floor", "round"])
    private body(@Arg("START TIME of currently RUNNING/ONGOING/LIVE candle", "m", false) ReferenceTimeFrame: ReferencetimeframeType,
        @Arg("Chart time frame ", "15m", false) TimeFrame: TimeframeType,
        @Arg("Candle number", 0, false) CandleNumber: CandlenumberType) { }


    @Function("Represents the shadow of the candle", true, true, ["collect", "change", "percentchange", "abs", "ceil", "floor", "round"])
    private shadow(@Arg("START TIME of currently RUNNING/ONGOING/LIVE candle", "m", false) ReferenceTimeFrame: ReferencetimeframeType,
        @Arg("Chart time frame ", "15m", false) TimeFrame: TimeframeType,
        @Arg("Candle number", 0, false) CandleNumber: CandlenumberType) { }

    @Function("Represents a candle", true, true, ["macd", "rsi"])
    private candle(@Arg("START TIME of currently RUNNING/ONGOING/LIVE candle", "m", false) ReferenceTimeFrame: ReferencetimeframeType,
        @Arg("Chart time frame ", "15m", false) TimeFrame: TimeframeType,
        @Arg("Candle number", 0, false) CandleNumber: CandlenumberType) { }

    @Function("Collects a number values from the defined starting candle upto specified count either in forward or backward direction", false, false, ["get", "max", "min", "sum", "average", "median", "mode", "range", "variance", "stdDev", "sma", "ema", "trendline", "LinearRegline", "valueAt"])
    private collect(@Arg("No. of candles", 100, false) NoOfCandles: NoofcandlesType,
        @Arg("Direction to collect the candles", "BACKWARD", true) Direction: DirectionType) { }


    @Function("Calculates the signal line for all the indicators which have signal lines and gives details about crossovers (Bullish/Bearish crossovers, crossover counts)", false, false, ["crossover", "crossoverCount", "valueAt"])
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

    @Function("Calculates the 'macd line' for given fast ema period and slow ema period.", false, false, ["collect", "stream", "valueAt", "abs", "ceil", "floor", "round"])
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


    @Function("Calculates the rsi value for given period. close values are used as default values for calculating rsi value, but user can calculate rsi of any of OLHCV values.", false, false, ["collect", "valueAt", "abs", "ceil", "floor", "round"])
    private rsi(@Arg("number of period to use for RSI Calculation", 14, false) period: PeriodType,
        @Arg("one of the olhcv for macd calculation, default of this indicator is 'close' value", "CLOSE", true) olhcvPrice: OlhcvpriceType) { }
}
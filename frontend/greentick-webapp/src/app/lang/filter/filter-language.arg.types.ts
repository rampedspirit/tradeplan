import { LArgumentType } from "./filter-language.types";

export class TimeframeType implements LArgumentType {
    regex: RegExp = /([1-9][0-9]?[mhdWM])/;
    valueProposals: string[] = ["1m", "2m", "3m", "4m", "5m", "10m", "15m", "30m", "1h", "2h", "3h", "1d", "1W", "1M"];
}

export class CandlenumberType implements LArgumentType {
    regex: RegExp = /([-]?[0-9][[0-9]?[0-9]?)/;
    valueProposals: number[] = [0, -1, -5, 1, 5];
}

export class ReferencetimeframeType implements LArgumentType {
    regex: RegExp = /[mhdWM]/;
    valueProposals: string[] = ["m", "h", "d", "W", "M"];
}

export class SymbolType implements LArgumentType {
    regex: RegExp = /[_0-9A-Z]+/;
    valueProposals: string[] = ["INFY", "TCS"];
}

export class NoofcandlesType implements LArgumentType {
    regex: RegExp = /[1-9][0-9]?[0-9]?/;
    valueProposals: number[] = [50, 100];
}

export class DirectionType implements LArgumentType {
    regex: RegExp = /FORWARD|forward|BACKWARD|backward/;
    valueProposals: string[] = ["FORWARD", "BACKWARD"];
}

export class OlhcvpriceType implements LArgumentType {
    regex: RegExp = /OPEN|open|LOW|low|HIGH|high|CLOSE|close|VOLUME|volume/;
    valueProposals: string[] = ["OPEN", "LOW", "HIGH", "CLOSE", "VOLUME"];
}

export class IndexType implements LArgumentType {
    regex: RegExp = /([-]?[0-9][[0-9]?[0-9]?)/;
    valueProposals: number[] = [0, 1, 2, 3];
}

export class RankType implements LArgumentType {
    regex: RegExp = /[1-9][0-9]?[0-9]?/;
    valueProposals: number[] = [1, 10];
}

export class ConditionType implements LArgumentType {
    regex: RegExp = /MSL|ZCO/;
    valueProposals: string[] = ["MSL", "ZCO"];
}

export class DegreeType implements LArgumentType {
    regex: RegExp = /[-2,-1,0,1,2]/;
    valueProposals: number[] = [-2, -1, 0, 1, 2];
}

export class FastperiodType implements LArgumentType {
    regex: RegExp = /([1-9][[0-9]?[0-9]?)/;
    valueProposals: number[] = [9, 12, 26, 50, 90];
}

export class SlowperiodType implements LArgumentType {
    regex: RegExp = /([1-9][[0-9]?[0-9]?)/;
    valueProposals: number[] = [9, 12, 26, 50, 90];
}

export class NumberofperiodType implements LArgumentType {
    regex: RegExp = /([1-9][[0-9]?[0-9]?)/;
    valueProposals: number[] = [9, 12, 26, 50, 90];
}

export class TypeType implements LArgumentType {
    regex: RegExp = /BULLISH|BEARISH|ANY/;
    valueProposals: string[] = ["BULLISH", "BEARISH", "ANY"];
}

export class NumberofstocksType implements LArgumentType {
    regex: RegExp = /[_A-Z0-9]+/;
    valueProposals: string[] = ["NIFTY_50", "NIFTY_IT", "NIFTY_ALPHA_50", "NIFTY_ENERGY", "NIFTY_PSE"];
}

export class PeriodType implements LArgumentType {
    regex: RegExp = /([1-9][[0-9]?[0-9]?)/;
    valueProposals: number[] = [9, 14, 25, 50, 90];
}

export class MovingaveragetypeType implements LArgumentType {
    regex: RegExp = /SMA|EMA/;
    valueProposals: string[] = ["SMA", "EMA"];
}

export class SignalPeriodType implements LArgumentType {
    regex: RegExp = /([1-9][[0-9]?[0-9]?)/;
    valueProposals: number[] = [9, 14, 25, 50, 90];
}

export class MultiplierType implements LArgumentType {
    regex: RegExp = /([1-9][[0-9]?[0-9]?)/;
    valueProposals: number[] = [9, 14, 25, 50, 90];
}


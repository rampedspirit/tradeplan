export interface LArgumentType {
    regex: RegExp;
    valueProposals: any[];
}

export interface LArgumentInfo {
    description: string;
    isOptional: boolean;
    defaultValue: any;
}

export interface LArgument extends LArgumentType, LArgumentInfo {
    name: string;
}

export interface LFunction {
    name: string;
    description: string;
    moreInfo:string;
    isStandalone: boolean;
    isChainOptional: boolean;
    arguments: LArgument[];
    nextFunctions: string[];
}

export interface LOperator {
    symbol: string;
    name: string;
}
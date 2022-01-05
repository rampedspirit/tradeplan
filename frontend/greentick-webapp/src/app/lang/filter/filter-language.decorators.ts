import { LArgument, LArgumentInfo, LArgumentType, LFunction, LOperator } from "./filter-language.types";

const OPERATOR_KEYS: Map<string, Symbol> = new Map();
const ARGUMENT_INFO_REGISTRY_KEY: Symbol = Symbol.for("argumentInfoRegistry");
const FUNCTIONS_KEY: Symbol = Symbol.for("functions");

/**
 * Decorator for Operator
 * @param symbol 
 * @param description 
 * @returns 
 */
export function Operator(symbol: string, name: string) {
    return function (target: any, propertyKey: string, descriptor: PropertyDescriptor) {
        if (!OPERATOR_KEYS.has(propertyKey)) {
            OPERATOR_KEYS.set(propertyKey, Symbol.for(propertyKey));
        }

        let key: Symbol = OPERATOR_KEYS.get(propertyKey);
        let operators: LOperator[] = Reflect.getOwnMetadata(key, target, propertyKey) || [];
        operators.push({
            symbol: symbol,
            name: name
        });
        Reflect.defineMetadata(key, operators, target, propertyKey);
    }
}

/**
 * Returns all the Operators for the given property key
 * @param target 
 * @param propertyKey 
 * @returns 
 */
export function getOperators(target: any, propertyKey: string): LOperator[] {
    let key: Symbol = OPERATOR_KEYS.get(propertyKey);
    return Reflect.getOwnMetadata(key, target, propertyKey);
}

/**
 * Decorator for Function
 * @param description 
 * @param isStandalone default false
 * @param isChainOptional default true
 * @returns 
 */
export function Function(description: string, isStandalone = false, isChainOptional = true, nextFunctions: string[] = []) {
    return function (target: any, propertyKey: string, descriptor: PropertyDescriptor) {
        let functions: LFunction[] = Reflect.getOwnMetadata(FUNCTIONS_KEY, target, FUNCTIONS_KEY.valueOf()) || [];
        let argumentInfoRegistry: Map<string, LArgumentInfo> = Reflect.getOwnMetadata(ARGUMENT_INFO_REGISTRY_KEY, target, ARGUMENT_INFO_REGISTRY_KEY.valueOf()) || new Map();
        let argumentTypes: (new () => LArgumentType)[] = Reflect.getMetadata("design:paramtypes", target, propertyKey);
        let matches: RegExpMatchArray = descriptor.value.toString().match(propertyKey + "\\((.*)\\)");
        let argumentNames: string[] = matches[1].split(",").filter(arg => arg.trim());
        let args: LArgument[] = [];
        for (let i = 0; i < argumentNames.length; i++) {
            let argumentInfo: LArgumentInfo = argumentInfoRegistry.get(propertyKey + "." + i);
            let argumentType: LArgumentType = new argumentTypes[i]();
            args.push({
                name: argumentNames[i].trim(),
                description: argumentInfo.description,
                defaultValue: argumentInfo.defaultValue,
                isOptional: argumentInfo.isOptional,
                regex: argumentType.regex,
                valueProposals: argumentType.valueProposals
            });
        }

        functions.push({
            name: propertyKey,
            description: description,
            isStandalone: isStandalone,
            isChainOptional: isChainOptional,
            arguments: args,
            nextFunctions: nextFunctions
        })
        Reflect.defineMetadata(FUNCTIONS_KEY, functions, target, FUNCTIONS_KEY.valueOf());
    }
}

/**
 * Returns all the Functions
 * @param target 
 * @returns 
 */
export function getFunctions(target: any): LFunction[] {
    return Reflect.getOwnMetadata(FUNCTIONS_KEY, target, FUNCTIONS_KEY.valueOf());
}

/**
 * Decorator for Function Argument
 * @param description 
 * @param defaultValue 
 * @param isOptional default false
 * @returns 
 */
export function Arg(description: string, defaultValue: any, isOptional = false) {
    return function (target: Object, propertyKey: string, parameterIndex: number) {
        let argumentRegistry: Map<string, LArgumentInfo> = Reflect.getOwnMetadata(ARGUMENT_INFO_REGISTRY_KEY, target, ARGUMENT_INFO_REGISTRY_KEY.valueOf()) || new Map();
        argumentRegistry.set(propertyKey + "." + parameterIndex, {
            description: description,
            defaultValue: defaultValue,
            isOptional: isOptional
        });
        Reflect.defineMetadata(ARGUMENT_INFO_REGISTRY_KEY, argumentRegistry, target, ARGUMENT_INFO_REGISTRY_KEY.valueOf());
    }
}
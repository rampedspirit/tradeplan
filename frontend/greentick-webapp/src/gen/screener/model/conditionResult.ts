/**
 * Screener
 * These are Screener service APIs
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

export interface ConditionResult { 
    /**
     * unique identifier of the screener to which execution is associated.
     */
    screenerId?: string;
    /**
     * unique identifier of the execution, using which result can be obtained
     */
    executionId?: string;
    /**
     * condition result  identifier.
     */
    conditionResultId: string;
    /**
     * name of scrip.
     */
    scripName?: string;
    /**
     * market time at which screener to be executed.
     */
    result?: ConditionResult.ResultEnum;
    /**
     * condition code with filter details tagged.
     */
    conditionCode?: string;
}
export namespace ConditionResult {
    export type ResultEnum = 'PASS' | 'FAIL' | 'EVALUATING' | 'INVALID';
    export const ResultEnum = {
        PASS: 'PASS' as ResultEnum,
        FAIL: 'FAIL' as ResultEnum,
        EVALUATING: 'EVALUATING' as ResultEnum,
        INVALID: 'INVALID' as ResultEnum
    };
}
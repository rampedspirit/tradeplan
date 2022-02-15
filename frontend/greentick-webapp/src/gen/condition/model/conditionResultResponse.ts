/**
 * Condition
 * These are condition service APIs
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
import { FilterResult } from './filterResult';

export interface ConditionResultResponse { 
    /**
     * unique identifier of the condition.
     */
    id: string;
    /**
     * name of condition
     */
    name: string;
    /**
     * description of condition
     */
    description?: string;
    /**
     * text representing condition code
     */
    code?: string;
    /**
     * market time at which screener to be executed.
     */
    marketTime?: Date;
    scripName?: string;
    conditionResult?: ConditionResultResponse.ConditionResultEnum;
    /**
     * associated filters.
     */
    filtersResult?: Array<FilterResult>;
}
export namespace ConditionResultResponse {
    export type ConditionResultEnum = 'QUEUED' | 'RUNNING' | 'PASS' | 'FAIL' | 'ERROR';
    export const ConditionResultEnum = {
        QUEUED: 'QUEUED' as ConditionResultEnum,
        RUNNING: 'RUNNING' as ConditionResultEnum,
        PASS: 'PASS' as ConditionResultEnum,
        FAIL: 'FAIL' as ConditionResultEnum,
        ERROR: 'ERROR' as ConditionResultEnum
    };
}
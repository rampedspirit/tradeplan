/**
 * Watchlist
 * These are Watchlist service APIs
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

export interface WatchlistPatchData { 
    /**
     * Action to be performed on given resource. 
     */
    operation: WatchlistPatchData.OperationEnum;
    /**
     * The property of the json attribute to be replaced.
     */
    property: WatchlistPatchData.PropertyEnum;
    /**
     * The new value of the json attribute to be patched.
     */
    value: string;
}
export namespace WatchlistPatchData {
    export type OperationEnum = 'REPLACE';
    export const OperationEnum = {
        REPLACE: 'REPLACE' as OperationEnum
    };
    export type PropertyEnum = 'NAME' | 'DESCRIPTION' | 'SCRIP_NAMES';
    export const PropertyEnum = {
        NAME: 'NAME' as PropertyEnum,
        DESCRIPTION: 'DESCRIPTION' as PropertyEnum,
        SCRIPNAMES: 'SCRIP_NAMES' as PropertyEnum
    };
}
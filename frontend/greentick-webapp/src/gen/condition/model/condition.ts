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

export interface Condition { 
    /**
     * unique identifier of the condition. It is auto-generated by system, API-client need not provide it during creation of screener. If provided the value will be ignored.
     */
    id?: string;
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
     * output after parsing grammar
     */
    parseTree?: string;
}
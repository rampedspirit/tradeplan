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

export interface ModelError { 
    /**
     * brief information about error
     */
    message?: string;
    /**
     * possibly hints to solve the error
     */
    resolution?: string;
    /**
     * pattern to unqiuely identify error root cause and origin.
     */
    errorCode?: string;
}
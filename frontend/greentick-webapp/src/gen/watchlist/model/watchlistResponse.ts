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

export interface WatchlistResponse { 
    /**
     * watchlist identifier
     */
    watchlistId?: string;
    /**
     * name of watchlist
     */
    name: string;
    /**
     * description of watchlist
     */
    description?: string;
    /**
     * scrip names.
     */
    scripNames?: Array<string>;
}
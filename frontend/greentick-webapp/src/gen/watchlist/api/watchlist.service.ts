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
 *//* tslint:disable:no-unused-variable member-ordering */

import { Inject, Injectable, Optional }                      from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams,
         HttpResponse, HttpEvent }                           from '@angular/common/http';
import { CustomHttpUrlEncodingCodec }                        from '../encoder';

import { Observable }                                        from 'rxjs';

import { WatchlistCreateRequest } from '../model/watchlistCreateRequest';
import { WatchlistPatchData } from '../model/watchlistPatchData';
import { WatchlistResponse } from '../model/watchlistResponse';

import { BASE_PATH, COLLECTION_FORMATS }                     from '../variables';
import { Configuration }                                     from '../configuration';


@Injectable()
export class WatchlistService {

    protected basePath = 'http://localhost:5004/';
    public defaultHeaders = new HttpHeaders();
    public configuration = new Configuration();

    constructor(protected httpClient: HttpClient, @Optional()@Inject(BASE_PATH) basePath: string, @Optional() configuration: Configuration) {
        if (basePath) {
            this.basePath = basePath;
        }
        if (configuration) {
            this.configuration = configuration;
            this.basePath = basePath || configuration.basePath || this.basePath;
        }
    }

    /**
     * @param consumes string[] mime-types
     * @return true: consumes contains 'multipart/form-data', false: otherwise
     */
    private canConsumeForm(consumes: string[]): boolean {
        const form = 'multipart/form-data';
        for (const consume of consumes) {
            if (form === consume) {
                return true;
            }
        }
        return false;
    }


    /**
     * create new watchlist
     * create new watchlist
     * @param body payload to create watchlist
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public createWatchlist(body: WatchlistCreateRequest, observe?: 'body', reportProgress?: boolean): Observable<WatchlistResponse>;
    public createWatchlist(body: WatchlistCreateRequest, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<WatchlistResponse>>;
    public createWatchlist(body: WatchlistCreateRequest, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<WatchlistResponse>>;
    public createWatchlist(body: WatchlistCreateRequest, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        if (body === null || body === undefined) {
            throw new Error('Required parameter body was null or undefined when calling createWatchlist.');
        }

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json'
        ];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        const consumes: string[] = [
            'application/json'
        ];
        const httpContentTypeSelected: string | undefined = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected != undefined) {
            headers = headers.set('Content-Type', httpContentTypeSelected);
        }

        return this.httpClient.request<WatchlistResponse>('post',`${this.basePath}/v1/watchlist`,
            {
                body: body,
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * delete
     * delete watchlist of given id
     * @param watchlistId 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public deleteWatchlist(watchlistId: string, observe?: 'body', reportProgress?: boolean): Observable<WatchlistResponse>;
    public deleteWatchlist(watchlistId: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<WatchlistResponse>>;
    public deleteWatchlist(watchlistId: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<WatchlistResponse>>;
    public deleteWatchlist(watchlistId: string, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        if (watchlistId === null || watchlistId === undefined) {
            throw new Error('Required parameter watchlistId was null or undefined when calling deleteWatchlist.');
        }

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json'
        ];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        const consumes: string[] = [
        ];

        return this.httpClient.request<WatchlistResponse>('delete',`${this.basePath}/v1/watchlist/${encodeURIComponent(String(watchlistId))}`,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * get all watchlist
     * get watchlist
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public getAllWatchlists(observe?: 'body', reportProgress?: boolean): Observable<Array<WatchlistResponse>>;
    public getAllWatchlists(observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<Array<WatchlistResponse>>>;
    public getAllWatchlists(observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<Array<WatchlistResponse>>>;
    public getAllWatchlists(observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json'
        ];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        const consumes: string[] = [
        ];

        return this.httpClient.request<Array<WatchlistResponse>>('get',`${this.basePath}/v1/watchlist`,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * get watchlist of given id
     * get watchlist
     * @param watchlistId 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public getWatchlist(watchlistId: string, observe?: 'body', reportProgress?: boolean): Observable<WatchlistResponse>;
    public getWatchlist(watchlistId: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<WatchlistResponse>>;
    public getWatchlist(watchlistId: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<WatchlistResponse>>;
    public getWatchlist(watchlistId: string, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        if (watchlistId === null || watchlistId === undefined) {
            throw new Error('Required parameter watchlistId was null or undefined when calling getWatchlist.');
        }

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json'
        ];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        const consumes: string[] = [
        ];

        return this.httpClient.request<WatchlistResponse>('get',`${this.basePath}/v1/watchlist/${encodeURIComponent(String(watchlistId))}`,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * update watchlist
     * update watchlist of given id
     * @param body Payload to change watchlist of given Id.
     * @param watchlistId 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public updateWatchlist(body: Array<WatchlistPatchData>, watchlistId: string, observe?: 'body', reportProgress?: boolean): Observable<WatchlistResponse>;
    public updateWatchlist(body: Array<WatchlistPatchData>, watchlistId: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<WatchlistResponse>>;
    public updateWatchlist(body: Array<WatchlistPatchData>, watchlistId: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<WatchlistResponse>>;
    public updateWatchlist(body: Array<WatchlistPatchData>, watchlistId: string, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        if (body === null || body === undefined) {
            throw new Error('Required parameter body was null or undefined when calling updateWatchlist.');
        }

        if (watchlistId === null || watchlistId === undefined) {
            throw new Error('Required parameter watchlistId was null or undefined when calling updateWatchlist.');
        }

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json'
        ];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        const consumes: string[] = [
            'application/json'
        ];
        const httpContentTypeSelected: string | undefined = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected != undefined) {
            headers = headers.set('Content-Type', httpContentTypeSelected);
        }

        return this.httpClient.request<WatchlistResponse>('patch',`${this.basePath}/v1/watchlist/${encodeURIComponent(String(watchlistId))}`,
            {
                body: body,
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

}

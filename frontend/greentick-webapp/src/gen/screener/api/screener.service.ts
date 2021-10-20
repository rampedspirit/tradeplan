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
 *//* tslint:disable:no-unused-variable member-ordering */

import { Inject, Injectable, Optional }                      from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams,
         HttpResponse, HttpEvent }                           from '@angular/common/http';
import { CustomHttpUrlEncodingCodec }                        from '../encoder';

import { Observable }                                        from 'rxjs';

import { PatchModel } from '../model/patchModel';
import { ScreenerRequest } from '../model/screenerRequest';
import { ScreenerResponse } from '../model/screenerResponse';

import { BASE_PATH, COLLECTION_FORMATS }                     from '../variables';
import { Configuration }                                     from '../configuration';


@Injectable()
export class ScreenerService {

    protected basePath = 'http://localhost:5000/';
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
     * create new screener
     * create new screener
     * @param body payload to create screener
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public createScreener(body: ScreenerRequest, observe?: 'body', reportProgress?: boolean): Observable<ScreenerResponse>;
    public createScreener(body: ScreenerRequest, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<ScreenerResponse>>;
    public createScreener(body: ScreenerRequest, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<ScreenerResponse>>;
    public createScreener(body: ScreenerRequest, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        if (body === null || body === undefined) {
            throw new Error('Required parameter body was null or undefined when calling createScreener.');
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

        return this.httpClient.request<ScreenerResponse>('post',`${this.basePath}/v1/screener`,
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
     * delete screener of given id
     * @param screenerId 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public deleteScreener(screenerId: string, observe?: 'body', reportProgress?: boolean): Observable<ScreenerResponse>;
    public deleteScreener(screenerId: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<ScreenerResponse>>;
    public deleteScreener(screenerId: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<ScreenerResponse>>;
    public deleteScreener(screenerId: string, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        if (screenerId === null || screenerId === undefined) {
            throw new Error('Required parameter screenerId was null or undefined when calling deleteScreener.');
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

        return this.httpClient.request<ScreenerResponse>('delete',`${this.basePath}/v1/screener/${encodeURIComponent(String(screenerId))}`,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * get all screener
     * get screener
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public getAllScreeners(observe?: 'body', reportProgress?: boolean): Observable<Array<ScreenerResponse>>;
    public getAllScreeners(observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<Array<ScreenerResponse>>>;
    public getAllScreeners(observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<Array<ScreenerResponse>>>;
    public getAllScreeners(observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

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

        return this.httpClient.request<Array<ScreenerResponse>>('get',`${this.basePath}/v1/screener`,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * get screener of given id
     * get screener
     * @param screenerId 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public getScreener(screenerId: string, observe?: 'body', reportProgress?: boolean): Observable<ScreenerResponse>;
    public getScreener(screenerId: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<ScreenerResponse>>;
    public getScreener(screenerId: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<ScreenerResponse>>;
    public getScreener(screenerId: string, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        if (screenerId === null || screenerId === undefined) {
            throw new Error('Required parameter screenerId was null or undefined when calling getScreener.');
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

        return this.httpClient.request<ScreenerResponse>('get',`${this.basePath}/v1/screener/${encodeURIComponent(String(screenerId))}`,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * update screener
     * update screener of given id
     * @param body Payload to change screener of given Id. Only Name, description, watchlist and condition of the screener can be changed. It is also important to note, change of watchlist or condition will remove associated execution result of the screener.
     * @param screenerId 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public updateScreener(body: PatchModel, screenerId: string, observe?: 'body', reportProgress?: boolean): Observable<ScreenerResponse>;
    public updateScreener(body: PatchModel, screenerId: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<ScreenerResponse>>;
    public updateScreener(body: PatchModel, screenerId: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<ScreenerResponse>>;
    public updateScreener(body: PatchModel, screenerId: string, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        if (body === null || body === undefined) {
            throw new Error('Required parameter body was null or undefined when calling updateScreener.');
        }

        if (screenerId === null || screenerId === undefined) {
            throw new Error('Required parameter screenerId was null or undefined when calling updateScreener.');
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

        return this.httpClient.request<ScreenerResponse>('patch',`${this.basePath}/v1/screener/${encodeURIComponent(String(screenerId))}`,
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

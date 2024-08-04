/* eslint-disable */
/* tslint:disable */
/*
 * ---------------------------------------------------------------
 * ## THIS FILE WAS GENERATED VIA SWAGGER-TYPESCRIPT-API        ##
 * ##                                                           ##
 * ## AUTHOR: acacode                                           ##
 * ## SOURCE: https://github.com/acacode/swagger-typescript-api ##
 * ---------------------------------------------------------------
 */

export interface UserRegisterRequest {
  userAccount?: string;
  userPassword?: string;
  checkPassword?: string;
}

export interface BaseResponseLong {
  /** @format int32 */
  code?: number;
  /** @format int64 */
  data?: number;
  message?: string;
}

export interface BaseResponseBoolean {
  /** @format int32 */
  code?: number;
  data?: boolean;
  message?: string;
}

export interface UserLoginRequest {
  userAccount?: string;
  userPassword?: string;
}

export interface BaseResponseUserVO {
  /** @format int32 */
  code?: number;
  data?: UserVO;
  message?: string;
}

export interface UserVO {
  /** @format int64 */
  id?: number;
  userName?: string;
  /** @format date-time */
  createTime?: string;
  /** @format date-time */
  updateTime?: string;
}

export interface ChartQueryRequest {
  /** @format int64 */
  id?: number;
  name?: string;
  goal?: string;
  chartType?: string;
  /** @format int64 */
  userId?: number;
  /** @format int32 */
  pageNum?: number;
  /** @format int32 */
  pageSize?: number;
}

export interface BaseResponseListChartVO {
  /** @format int32 */
  code?: number;
  data?: ChartVO[];
  message?: string;
}

export interface ChartVO {
  /** @format int64 */
  id?: number;
  chartType?: string;
  name?: string;
  goal?: string;
  genChart?: string;
  genResult?: string;
}

export interface DoChatRequest {
  name?: string;
  question?: string;
  data?: string;
  chatType?: string;
}

export interface BaseResponseDoChatResponse {
  /** @format int32 */
  code?: number;
  data?: DoChatResponse;
  message?: string;
}

export interface DoChatResponse {
  /** @format int64 */
  chartId?: number;
  option?: string;
  message?: string;
}

import type { AxiosInstance, AxiosRequestConfig, AxiosResponse, HeadersDefaults, ResponseType } from "axios";
import axios from "axios";

export type QueryParamsType = Record<string | number, any>;

export interface FullRequestParams extends Omit<AxiosRequestConfig, "data" | "params" | "url" | "responseType"> {
  /** set parameter to `true` for call `securityWorker` for this request */
  secure?: boolean;
  /** request path */
  path: string;
  /** content type of request body */
  type?: ContentType;
  /** query params */
  query?: QueryParamsType;
  /** format of response (i.e. response.json() -> format: "json") */
  format?: ResponseType;
  /** request body */
  body?: unknown;
}

export type RequestParams = Omit<FullRequestParams, "body" | "method" | "query" | "path">;

export interface ApiConfig<SecurityDataType = unknown> extends Omit<AxiosRequestConfig, "data" | "cancelToken"> {
  securityWorker?: (
    securityData: SecurityDataType | null,
  ) => Promise<AxiosRequestConfig | void> | AxiosRequestConfig | void;
  secure?: boolean;
  format?: ResponseType;
}

export enum ContentType {
  Json = "application/json",
  FormData = "multipart/form-data",
  UrlEncoded = "application/x-www-form-urlencoded",
  Text = "text/plain",
}

export class HttpClient<SecurityDataType = unknown> {
  public instance: AxiosInstance;
  private securityData: SecurityDataType | null = null;
  private securityWorker?: ApiConfig<SecurityDataType>["securityWorker"];
  private secure?: boolean;
  private format?: ResponseType;

  constructor({ securityWorker, secure, format, ...axiosConfig }: ApiConfig<SecurityDataType> = {}) {
    this.instance = axios.create({ ...axiosConfig, baseURL: axiosConfig.baseURL || "http://localhost:8080" });
    this.secure = secure;
    this.format = format;
    this.securityWorker = securityWorker;
  }

  public setSecurityData = (data: SecurityDataType | null) => {
    this.securityData = data;
  };

  protected mergeRequestParams(params1: AxiosRequestConfig, params2?: AxiosRequestConfig): AxiosRequestConfig {
    const method = params1.method || (params2 && params2.method);

    return {
      ...this.instance.defaults,
      ...params1,
      ...(params2 || {}),
      headers: {
        ...((method && this.instance.defaults.headers[method.toLowerCase() as keyof HeadersDefaults]) || {}),
        ...(params1.headers || {}),
        ...((params2 && params2.headers) || {}),
      },
    };
  }

  protected stringifyFormItem(formItem: unknown) {
    if (typeof formItem === "object" && formItem !== null) {
      return JSON.stringify(formItem);
    } else {
      return `${formItem}`;
    }
  }

  protected createFormData(input: Record<string, unknown>): FormData {
    if (input instanceof FormData) {
      return input;
    }
    return Object.keys(input || {}).reduce((formData, key) => {
      const property = input[key];
      const propertyContent: any[] = property instanceof Array ? property : [property];

      for (const formItem of propertyContent) {
        const isFileType = formItem instanceof Blob || formItem instanceof File;
        formData.append(key, isFileType ? formItem : this.stringifyFormItem(formItem));
      }

      return formData;
    }, new FormData());
  }

  public request = async <T = any, _E = any>({
    secure,
    path,
    type,
    query,
    format,
    body,
    ...params
  }: FullRequestParams): Promise<AxiosResponse<T>> => {
    const secureParams =
      ((typeof secure === "boolean" ? secure : this.secure) &&
        this.securityWorker &&
        (await this.securityWorker(this.securityData))) ||
      {};
    const requestParams = this.mergeRequestParams(params, secureParams);
    const responseFormat = format || this.format || undefined;

    if (type === ContentType.FormData && body && body !== null && typeof body === "object") {
      body = this.createFormData(body as Record<string, unknown>);
    }

    if (type === ContentType.Text && body && body !== null && typeof body !== "string") {
      body = JSON.stringify(body);
    }

    return this.instance.request({
      ...requestParams,
      headers: {
        ...(requestParams.headers || {}),
        ...(type ? { "Content-Type": type } : {}),
      },
      params: query,
      responseType: responseFormat,
      data: body,
      url: path,
    });
  };
}

/**
 * @title OpenAPI definition
 * @version v0
 * @baseUrl http://localhost:8080
 */
export class Api<SecurityDataType extends unknown> extends HttpClient<SecurityDataType> {
  user = {
    /**
     * No description
     *
     * @tags User Controller
     * @name UserRegister
     * @request POST:/user/register
     */
    userRegister: (data: UserRegisterRequest, params: RequestParams = {}) =>
      this.request<BaseResponseLong, any>({
        path: `/user/register`,
        method: "POST",
        body: data,
        type: ContentType.Json,
        ...params,
      }),

    /**
     * No description
     *
     * @tags User Controller
     * @name UserLogout
     * @request POST:/user/logout
     */
    userLogout: (params: RequestParams = {}) =>
      this.request<BaseResponseBoolean, any>({
        path: `/user/logout`,
        method: "POST",
        ...params,
      }),

    /**
     * No description
     *
     * @tags User Controller
     * @name UserLogin
     * @request POST:/user/login
     */
    userLogin: (data: UserLoginRequest, params: RequestParams = {}) =>
      this.request<BaseResponseUserVO, any>({
        path: `/user/login`,
        method: "POST",
        body: data,
        type: ContentType.Json,
        ...params,
      }),
  };
  chart = {
    /**
     * No description
     *
     * @tags chart controller
     * @name GetAllByUid
     * @request POST:/chart/get_all
     */
    getAllByUid: (data: ChartQueryRequest, params: RequestParams = {}) =>
      this.request<BaseResponseListChartVO, any>({
        path: `/chart/get_all`,
        method: "POST",
        body: data,
        type: ContentType.Json,
        ...params,
      }),

    /**
     * No description
     *
     * @tags chart controller
     * @name GenerateChartByAi
     * @request POST:/chart/generate
     */
    generateChartByAi: (
      data: {
        /** @format binary */
        file: File;
        doChatRequest: DoChatRequest;
      },
      params: RequestParams = {},
    ) =>
      this.request<BaseResponseDoChatResponse, any>({
        path: `/chart/generate`,
        method: "POST",
        body: data,
        type: ContentType.FormData,
        ...params,
      }),

    /**
     * No description
     *
     * @tags chart controller
     * @name GenerateChartByAiMq
     * @request POST:/chart/generate/mq
     */
    generateChartByAiMq: (
      data: {
        /** @format binary */
        file: File;
        doChatRequest: DoChatRequest;
      },
      params: RequestParams = {},
    ) =>
      this.request<BaseResponseDoChatResponse, any>({
        path: `/chart/generate/mq`,
        method: "POST",
        body: data,
        type: ContentType.FormData,
        ...params,
      }),
  };
}

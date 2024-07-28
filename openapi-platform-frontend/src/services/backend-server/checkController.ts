// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** getParam GET /web/check/get-param */
export async function getParamUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getParamUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.CheckDto>('/web/check/get-param', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** postBody GET /web/check/post-body */
export async function postBodyUsingGet(body: API.CheckDto, options?: { [key: string]: any }) {
  return request<API.CheckDto>('/web/check/post-body', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** debug POST /web/invoke/debug */
export async function debugUsingPost(body: API.InvokeDto, options?: { [key: string]: any }) {
  return request<API.BaseResponse>('/web/invoke/debug', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

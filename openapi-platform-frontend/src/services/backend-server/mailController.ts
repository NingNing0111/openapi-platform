// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** sendVerifyCode POST /web/mail/verify-code */
export async function sendVerifyCodeUsingPost(
  body: API.VerifyCodeDto,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseString_>('/web/mail/verify-code', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

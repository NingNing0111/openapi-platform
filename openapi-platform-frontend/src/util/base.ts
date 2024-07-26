import * as crypto from 'crypto-js';

export const initApiHeader = (
  body: { [key: string]: string },
  secretKey: string,
  accessKey: string,
): { [key: string]: string } => {
  const timestamp = Date.now();
  const data = {
    accessKey: accessKey,
    body: JSON.stringify(body),
    timestamp: timestamp.toString(),
  };
  const content = `${JSON.stringify(data)}.${secretKey}`;
  console.log(content);

  const sign = crypto.SHA256(content).toString(crypto.enc.Hex);

  const newHeader = {
    ...data,
    sign: sign,
  };
  return newHeader;
};

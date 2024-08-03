import { SendOutlined } from '@ant-design/icons';
import { ProCard, ProDescriptions } from '@ant-design/pro-components';
import { Button, Input, Tag } from 'antd';
import RcResizeObserver from 'rc-resize-observer';
import { useState } from 'react';

export type Props = {
  requestParam: string; // 请求参数字符串
  requestResult: string; // 请求结果
  requestParamDes: string; // 参数描述
  onChange: (value: string) => void; // 请求参数变化时的Hook
  visible: boolean; // 调试框是否出现
  loading: boolean; // 加载
  doInvoke: () => Promise<void>;
  invokeStatus: string;
  isListShow: boolean;
};
const InvokeBox = (props: Props) => {
  const [responsive, setResponsive] = useState(false);
  const {
    loading,
    visible,
    requestParam,
    requestResult,
    requestParamDes,
    doInvoke,
    onChange,
    invokeStatus,
    isListShow,
  } = props;
  let result: any = {};

  if (requestResult === null || requestResult === '') {
    result = '{}';
  } else {
    try {
      const decodeString = decodeURIComponent(requestResult);
      result = JSON.parse(decodeString);
    } catch (err) {
      result = {};
    }
  }

  return (
    <RcResizeObserver
      key="resize-observer"
      onResize={(offset) => {
        setResponsive(offset.width < 596);
      }}
    >
      {visible && (
        <ProCard
          title="调试"
          extra={[
            invokeStatus === 'ok' ? (
              <Tag key={'invokeStatus'} color="success">
                成功
              </Tag>
            ) : (
              <Tag key={'invokeStatus'} color="error">
                失败
              </Tag>
            ),
            <Button
              icon={<SendOutlined />}
              type="primary"
              key={'invoke'}
              onClick={async () => await doInvoke()}
            >
              发送
            </Button>,
          ]}
          split={responsive ? 'horizontal' : 'vertical'}
          bordered
          headerBordered
        >
          <ProCard title="请求参数" colSpan="50%">
            <Input.TextArea
              onChange={(e) => onChange(e.target.value)}
              value={requestParam}
              autoSize={{
                minRows: 20,
                maxRows: 20,
              }}
              placeholder={JSON.stringify(JSON.parse(requestParamDes), null, 4)}
            />
          </ProCard>

          <ProCard loading={loading} title="响应结果" tooltip="包括响应状态码、响应头、响应结果">
            {isListShow ? (
              <ProDescriptions layout="vertical" column={1}>
                <ProDescriptions.Item key={'statusCode'} label="响应状态" valueType="text">
                  {JSON.stringify(result.statusCode) ?? '{}'}
                </ProDescriptions.Item>
                <ProDescriptions.Item
                  key={'statusCodeValue'}
                  label="状态码"
                  valueEnum={{
                    200: {
                      text: '200',
                      status: 'success',
                    },
                    404: {
                      text: '404',
                      status: 'error',
                    },
                    403: {
                      text: '403',
                      status: 'error',
                    },
                  }}
                >
                  {result.statusCodeValue ?? -1}
                </ProDescriptions.Item>
                <ProDescriptions.Item key={'body'} label="响应数据" valueType={'jsonCode'}>
                  {result.body ?? '{}'}
                </ProDescriptions.Item>
                <ProDescriptions.Item key={'响应头'} label="响应头" valueType={'jsonCode'}>
                  {JSON.stringify(result.headers) ?? '{}'}
                </ProDescriptions.Item>
              </ProDescriptions>
            ) : (
              <Input.TextArea
                onChange={(e) => onChange(e.target.value)}
                value={requestResult}
                autoSize={{
                  minRows: 20,
                  maxRows: 20,
                }}
                placeholder={requestResult}
              />
            )}
          </ProCard>
        </ProCard>
      )}
    </RcResizeObserver>
  );
};

export default InvokeBox;

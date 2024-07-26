import { SendOutlined } from '@ant-design/icons';
import { ProCard } from '@ant-design/pro-components';
import { Button, Input, Tag } from 'antd';
import RcResizeObserver from 'rc-resize-observer';
import { useState } from 'react';

export type Props = {
  requestParam: string; // 请求参数字符串
  requestResult: string; // 请求结果
  requestParamDes: string;
  onChange: (value: string) => void; // 请求参数变化时的Hook
  visible: boolean; // 调试框是否出现
  loading: boolean; // 加载
  doInvoke: () => void;
  invokeStatus: string;
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
  } = props;
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
              onClick={() => doInvoke()}
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
          <ProCard title="响应结果" loading={loading}>
            <Input.TextArea
              value={requestResult}
              autoSize={{
                minRows: 20,
                maxRows: 20,
              }}
              style={{ height: 500 }}
              placeholder={'等待发送'}
            />
          </ProCard>
        </ProCard>
      )}
    </RcResizeObserver>
  );
};

export default InvokeBox;

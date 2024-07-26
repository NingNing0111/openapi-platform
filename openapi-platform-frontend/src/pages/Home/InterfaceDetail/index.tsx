import { getInterfaceInfoVoByIdUsingGet } from '@/services/backend-server/interfaceInfoController';
import { debugUsingPost } from '@/services/backend-server/invokeController';
import { initApiHeader } from '@/util/base';
import { SyncOutlined, ToolOutlined } from '@ant-design/icons';
import { PageContainer, ProDescriptions } from '@ant-design/pro-components';
import { useModel, useParams } from '@umijs/max';
import { Button, Card, message } from 'antd';
import { Divider } from 'antd/lib';
import { useEffect, useState } from 'react';
import InvokeBox from './component/InvokeBox';

const InterfaceDetail: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const [apiInfo, setApiInfo] = useState<API.InterfaceInfoVO>();
  const [invokeApiInfo, setInvokeApiInfo] = useState<API.InterfaceInfoVO>();
  const [openInvoke, setOpenInvoke] = useState(false);
  const [waiting, setWaiting] = useState(false);
  const [invokeResult, setInvokeResult] = useState('');
  const [invokeParam, setInvokeParam] = useState('');
  // 当前登录用户
  const { initialState, setInitialState } = useModel('@@initialState');

  // 获取路由参数
  const params = useParams();

  // 加载当前页的接口信息
  const loadApiInfo = async () => {
    if (!params.id) {
      message.error('接口信息不存在');
      return;
    }
    setLoading(true);

    try {
      const res = await getInterfaceInfoVoByIdUsingGet({
        id: Number(params.id),
      });
      setApiInfo(res.data);
      setInvokeApiInfo(res.data);
    } catch (err: any) {
      message.error('获取接口详细异常：' + err.message);
    }
    setLoading(false);
  };

  useEffect(() => {
    loadApiInfo();
    setInvokeResult('');
    setInvokeParam('');
    setOpenInvoke(false);
  }, []);

  // 处理调用请求
  const handleInvoke = async () => {
    setWaiting(true);
    setInvokeResult('');
    try {
      const params = JSON.parse(invokeParam);
      // 请求头
      const initHeader = initApiHeader(
        params,
        initialState?.loginUser?.secretKey ?? '',
        initialState?.loginUser?.accessKey ?? '',
      );
      const header = { ...initHeader, ...JSON.parse(apiInfo?.requestHeader ?? '') };
      const debugDto: API.InvokeDto = {
        requestHeaders: JSON.stringify(header),
        params: JSON.stringify(params),
        method: apiInfo?.method,
        accessKey: initialState?.loginUser?.accessKey,
        url: apiInfo?.url,
      };

      const res = await debugUsingPost(debugDto);
      if (res.code === 0) {
        setInvokeResult(JSON.stringify(res.data, null, 4));
      } else {
        setInvokeResult(JSON.stringify(res, null, 4));
      }
    } catch (err: any) {
      message.error('调试失败：' + err.message);
    }
    setWaiting(false);
  };

  return (
    <PageContainer loading={loading} title="接口详情" extra={[]}>
      <Card>
        {apiInfo ? (
          <ProDescriptions column={2} bordered={true}>
            <ProDescriptions.Item valueType={'option'}>
              <Button
                icon={<SyncOutlined />}
                iconPosition={'start'}
                type={'default'}
                key={'refresh'}
                onClick={async () => {
                  await loadApiInfo();
                  setInvokeResult('');
                  setInvokeParam('');
                  setOpenInvoke(false);
                }}
              >
                刷新
              </Button>
              <Button
                icon={<ToolOutlined />}
                iconPosition={'start'}
                type="primary"
                key={'debug'}
                onClick={() => setOpenInvoke(true)}
              >
                调试
              </Button>
            </ProDescriptions.Item>
            <ProDescriptions.Item label="名称" valueType="text">
              {apiInfo.name}
            </ProDescriptions.Item>
            <ProDescriptions.Item label="描述" valueType="textarea">
              {apiInfo.description}
            </ProDescriptions.Item>

            <ProDescriptions.Item
              label="请求方法"
              valueEnum={{
                GET: {
                  text: 'GET',
                  status: 'Success',
                },
                POST: {
                  text: 'POST',
                  status: 'Processing',
                },
                DELETE: {
                  text: 'DELETE',
                  status: 'Error',
                },
                PUT: {
                  text: 'PUT',
                  status: 'Warning',
                },
              }}
            >
              {/* {apiInfo.method === 'GET' && <Tabs color="success">{apiInfo.method}</Tabs>}
              {apiInfo.method === 'POST' && <Tabs color="processing">{apiInfo.method}</Tabs>}
              {apiInfo.method === 'DELETE' && <Tabs color="error">{apiInfo.method}</Tabs>}
              {apiInfo.method === 'PUT' && <Tabs color="warning">{apiInfo.method}</Tabs>} */}
              {apiInfo.method}
            </ProDescriptions.Item>
            <ProDescriptions.Item label="请求地址" valueType={'text'} copyable>
              <a href="#">{apiInfo.url}</a>
            </ProDescriptions.Item>
            <ProDescriptions.Item
              label="接口状态"
              valueEnum={{
                '1': {
                  text: '运行(可用)',
                  status: 'Success',
                },
                '0': {
                  text: '下线(不可用)',
                  status: 'Error',
                },
              }}
            >
              {apiInfo.status}
            </ProDescriptions.Item>
            <ProDescriptions.Item label="更新时间" valueType={'dateTime'}>
              {apiInfo.updateTime}
            </ProDescriptions.Item>

            <ProDescriptions.Item label="请求头" valueType={'jsonCode'}>
              {apiInfo.requestHeader}
            </ProDescriptions.Item>
            <ProDescriptions.Item label="响应头" valueType={'jsonCode'}>
              {apiInfo.responseHeader}
            </ProDescriptions.Item>
            <ProDescriptions.Item label="参数说明" valueType={'jsonCode'}>
              {apiInfo.requestParam}
            </ProDescriptions.Item>
          </ProDescriptions>
        ) : (
          <>接口信息不存在</>
        )}
      </Card>

      <Divider />

      <InvokeBox
        requestParam={invokeParam}
        requestResult={invokeResult}
        onChange={(value: string) => {
          setInvokeParam(value);
        }}
        visible={openInvoke}
        loading={waiting}
        doInvoke={async () => {
          handleInvoke();
        }}
        requestParamDes={invokeApiInfo?.requestParam ?? ''}
        invokeStatus="ok"
      />
    </PageContainer>
  );
};

export default InterfaceDetail;

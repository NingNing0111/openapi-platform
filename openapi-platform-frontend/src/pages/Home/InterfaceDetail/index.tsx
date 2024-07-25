import { getInterfaceInfoVoByIdUsingGet } from '@/services/backend-server/interfaceInfoController';
import { PageContainer } from '@ant-design/pro-components';
import { useParams } from '@umijs/max';
import { Button, Card, message } from 'antd';
import { Descriptions } from 'antd/lib';
import { useEffect, useState } from 'react';

const InterfaceDetail: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const [apiInfo, setApiInfo] = useState<API.InterfaceInfoVO>();

  // 获取路由参数
  const params = useParams();

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
    } catch (err: any) {
      message.error('获取接口详细异常：' + err.message);
    }
    setLoading(false);
  };

  useEffect(() => {
    loadApiInfo();
  }, []);

  return (
    <PageContainer
      title="接口详情"
      extra={[
        <Button type="link" size="large" key={'back'}>
          返回
        </Button>,
        <Button type="primary" key={'debug'}>
          调试
        </Button>,
      ]}
    >
      <Card>
        {apiInfo ? (
          <Descriptions title={apiInfo.name} column={1} layout={'vertical'}>
            <Descriptions.Item label="描述">{apiInfo.description}</Descriptions.Item>
            <Descriptions.Item label="地址">{apiInfo.url}</Descriptions.Item>
            <Descriptions.Item label="状态">{apiInfo.status}</Descriptions.Item>
            <Descriptions.Item label="方法">{apiInfo.method}</Descriptions.Item>
            <Descriptions.Item label="请求头">{apiInfo.requestHeader}</Descriptions.Item>
            <Descriptions.Item label="响应头">{apiInfo.responseHeader}</Descriptions.Item>
            <Descriptions.Item label="更新时间">{apiInfo.updateTime}</Descriptions.Item>
          </Descriptions>
        ) : (
          <>接口信息不存在</>
        )}
      </Card>
    </PageContainer>
  );
};

export default InterfaceDetail;

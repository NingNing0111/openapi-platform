import { listInterfaceInfoVoByPageUsingPost } from '@/services/backend-server/interfaceInfoController';
import { CheckCircleOutlined, CloseCircleOutlined } from '@ant-design/icons';
import { PageContainer } from '@ant-design/pro-components';
import { List, message, Tag } from 'antd';
import { useEffect, useState } from 'react';

const Index: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const [list, setList] = useState<API.InterfaceInfoVO[]>([]);
  const [total, setTotal] = useState<number>(0);
  const [selectPageSize, setSelectPageSize] = useState<number>(5);
  const loadApiInfoData = async (current = 0, pageSize = selectPageSize) => {
    setLoading(true);
    try {
      const res = await listInterfaceInfoVoByPageUsingPost({
        current,
        pageSize,
      });
      if (res.data) {
        setList(res.data.records ?? []);
        setTotal(res.data.total ?? 0);
      }
    } catch (err: any) {
      message.error('请求失败' + err.message);
    }
    setLoading(false);
  };

  useEffect(() => {
    loadApiInfoData();
  }, [selectPageSize]);

  return (
    <PageContainer title="PG Thinker's API 开放平台">
      <List
        className="home-list"
        loading={loading}
        itemLayout="horizontal"
        dataSource={list}
        renderItem={(item) => {
          const apiLink = `/interface-info/${item.id}`;

          return (
            <List.Item
              actions={[
                <a href={apiLink} key={item.id} target="_blank" rel="noopener noreferrer">
                  查看
                </a>,
              ]}
            >
              <List.Item.Meta
                title={
                  <a href={apiLink} target="_blank" rel="noopener noreferrer">
                    {item.name}
                  </a>
                }
                description={item.description}
              />
              {item.status === 0 ? (
                <Tag color="error" icon={<CloseCircleOutlined />}>
                  下线
                </Tag>
              ) : (
                <Tag color="success" icon={<CheckCircleOutlined />}>
                  正常
                </Tag>
              )}
            </List.Item>
          );
        }}
        pagination={{
          showSizeChanger: true,
          pageSizeOptions: [5, 10, 20],
          onShowSizeChange(current, size) {
            setSelectPageSize(size);
          },
          pageSize: selectPageSize,
          showTotal(t) {
            return `共计: ${t} 个接口`;
          },
          total: total,
          onChange(page, pageSize) {
            loadApiInfoData(page, pageSize);
          },
        }}
      />
    </PageContainer>
  );
};

export default Index;

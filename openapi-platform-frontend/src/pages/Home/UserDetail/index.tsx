import { getUserVoByIdUsingGet } from '@/services/backend-server/userController';
import { EditOutlined, SyncOutlined } from '@ant-design/icons';
import { PageContainer, ProCard, ProDescriptions } from '@ant-design/pro-components';
import { useModel } from '@umijs/max';
import { Avatar, Button, message } from 'antd';
import { useEffect, useState } from 'react';

const UserDetail = () => {
  const [loading, setLoading] = useState(false);
  const [accountInfo, setAccountInfo] = useState<API.UserVO>();
  const { initialState, setInitialState } = useModel('@@initialState');
  const loadAccountInfo = async () => {
    const currUserId = initialState?.loginUser?.id;
    setLoading(true);

    try {
      const res = await getUserVoByIdUsingGet({
        id: currUserId,
      });
      setAccountInfo(res.data);
    } catch (err: any) {
      message.error('用户信息获取异常：' + err.message);
    }
    setLoading(false);
  };

  useEffect(() => {
    loadAccountInfo();
  }, []);

  return (
    <PageContainer title="我的" loading={loading}>
      <ProCard>
        {accountInfo ? (
          <ProDescriptions column={2} title={'个人信息'} bordered={true}>
            <ProDescriptions.Item valueType={'option'}>
              <Button
                icon={<SyncOutlined />}
                iconPosition={'start'}
                type={'default'}
                key={'refresh'}
                onClick={loadAccountInfo}
              >
                刷新
              </Button>
              <Button icon={<EditOutlined />} iconPosition={'start'} type="primary" key={'edit'}>
                编辑
              </Button>
            </ProDescriptions.Item>
            <ProDescriptions.Item label="头像">
              <Avatar size={64} src={accountInfo.userAvatar} />
            </ProDescriptions.Item>
            <ProDescriptions.Item label="权限" valueType={'text'}>
              {accountInfo.userRole}
            </ProDescriptions.Item>

            <ProDescriptions.Item label="昵称" valueType={'text'}>
              {accountInfo.userName}
            </ProDescriptions.Item>
            <ProDescriptions.Item label="邮箱" valueType={'text'}>
              {accountInfo.email}
            </ProDescriptions.Item>

            <ProDescriptions.Item label="AccessKey" valueType={'text'} copyable>
              {accountInfo.accessKey}
            </ProDescriptions.Item>
            <ProDescriptions.Item label="SecretKey" valueType={'text'} copyable>
              {accountInfo.secretKey}
            </ProDescriptions.Item>
            <ProDescriptions.Item label="注册时间" valueType={'date'}>
              {accountInfo.createTime}
            </ProDescriptions.Item>
          </ProDescriptions>
        ) : (
          <h1>用户信息加载失败</h1>
        )}
      </ProCard>
    </PageContainer>
  );
};

export default UserDetail;

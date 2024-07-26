import {
  deleteUserUsingPost,
  listUserByPageUsingPost,
  updateUserUsingPost,
} from '@/services/backend-server/userController';
import { ExclamationCircleFilled } from '@ant-design/icons';
import { ActionType, PageContainer, ProColumns, ProTable } from '@ant-design/pro-components';
import { Button, message, Modal } from 'antd';
import { useRef, useState } from 'react';
import UpdateModal from './component/UpdateModal';
import './index.less';
const { confirm } = Modal;
const UserManager = () => {
  const [updateModalOpen, handleUpdateModalOpen] = useState<boolean>(false);
  const [currentRow, setCurrentRow] = useState<API.User>();
  const actionRef = useRef<ActionType>();
  type PageParams = {
    current: number;
    pageSize: number;
  };
  const roleEnum = {
    user: {
      text: '普通用户',
      status: 'Processing',
    },
    admin: {
      text: '管理员',
      status: 'Success',
    },
    ban: {
      text: '封禁',
      status: 'Default',
    },
  };

  // 封禁用户
  const handleChangeRoleUser = async (user: API.User, role: string) => {
    const hide = message.loading('修改中');
    try {
      await updateUserUsingPost({
        ...user,
        userRole: role,
      });
      hide();
      message.success('操作成功');
      actionRef.current?.reload();
      return true;
    } catch (error: any) {
      hide();
      message.error('操作失败' + error.message);
      return false;
    }
  };

  // 修改用户
  const handleUpdateUser = async (user: API.User) => {
    const hide = message.loading('修改中');
    try {
      await updateUserUsingPost({
        ...user,
        id: currentRow?.id,
      });
      hide();
      message.success('操作成功');
      actionRef.current?.reload();
      return true;
    } catch (error: any) {
      hide();
      message.error('操作失败' + error.message);
      return false;
    }
  };

  // 删除用户
  const handleDeleteUser = async (user: API.User) => {
    const hide = message.loading('删除中');
    try {
      await deleteUserUsingPost({
        id: user.id,
      });
      hide();
      message.success('删除成功');
      actionRef.current?.reload();
      return true;
    } catch (error: any) {
      hide();
      message.error('操作失败' + error.message);
      return false;
    }
  };

  const columns: ProColumns<API.User>[] = [
    {
      title: '用户名',
      dataIndex: 'userAccount',
      valueType: 'text',
      hideInForm: true,
      hideInSearch: true,
    },
    {
      title: '昵称',
      dataIndex: 'userName',
      valueType: 'text',
    },
    {
      title: '邮箱',
      dataIndex: 'email',
      valueType: 'text',
      hideInForm: true,
    },
    {
      title: '权限',
      dataIndex: 'userRole',
      valueEnum: roleEnum,
    },
    {
      title: '账号注册时间',
      dataIndex: 'createTime',
      valueType: 'dateTime',
      hideInForm: true,
      hideInSearch: true,
    },
    {
      title: '信息更新时间',
      dataIndex: 'updateTime',
      valueType: 'dateTime',
      hideInForm: true,
      hideInSearch: true,
    },
    {
      title: '操作',
      valueType: 'option',
      dataIndex: 'option',
      render: (_, record) => [
        record.userRole !== 'ban' ? (
          <Button
            type="text"
            danger
            key="ban"
            onClick={() => {
              confirm({
                title: '操作警告',
                icon: <ExclamationCircleFilled />,
                content: '确定封禁该用户吗？',
                okText: '确定',
                okType: 'danger',
                cancelText: '取消',
                onOk: async () => {
                  await handleChangeRoleUser(record, 'ban');
                },
                onCancel() {
                  console.log('Cancel');
                },
              });
            }}
          >
            封禁
          </Button>
        ) : (
          <Button
            type="text"
            className="warning-text"
            key="unBan"
            onClick={() => {
              confirm({
                title: '操作警告',
                icon: <ExclamationCircleFilled />,
                content: '确定解封该用户吗？',
                okText: '确定',
                okType: 'danger',
                cancelText: '取消',
                onOk: async () => {
                  await handleChangeRoleUser(record, 'user');
                },
                onCancel() {
                  console.log('Cancel');
                },
              });
            }}
          >
            解封
          </Button>
        ),
        <Button
          type="text"
          key="edit"
          className="success-text"
          onClick={() => {
            handleUpdateModalOpen(true);
            setCurrentRow(record);
          }}
        >
          修改
        </Button>,

        <Button
          type="text"
          danger
          key="delete"
          onClick={async (record) => {
            await handleDeleteUser(record);
          }}
          className="danger-text"
        >
          删除
        </Button>,
      ],
    },
  ];

  return (
    <PageContainer>
      <ProTable<API.UserVO, PageParams>
        cardBordered={{
          table: true,
        }}
        headerTitle="接口详情"
        actionRef={actionRef}
        rowKey="id"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => []}
        request={async (params) => {
          const res = await listUserByPageUsingPost({
            ...params,
          });
          if (res.data) {
            console.log(res.data);

            return {
              data: res.data.records || [],
              success: true,
              total: res.data.total,
            };
          } else {
            return {
              data: [],
              success: false,
              total: 0,
            };
          }
        }}
        columns={columns}
        pagination={{
          defaultCurrent: 1,
          defaultPageSize: 10,
          showTotal: (total) => `共 ${total} 条`,
          showSizeChanger: true,
          pageSizeOptions: ['10', '50', '100'],
        }}
      />

      <UpdateModal
        columns={columns}
        visible={updateModalOpen}
        initValue={currentRow || {}}
        onSubmit={async (value) => {
          await handleUpdateUser(value);
          handleUpdateModalOpen(false);
        }}
        onCancel={() => {
          handleUpdateModalOpen(false);
        }}
      />
    </PageContainer>
  );
};

export default UserManager;

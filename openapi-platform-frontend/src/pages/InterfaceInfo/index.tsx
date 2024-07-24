import {
  addInterfaceInfoUsingPost,
  deleteInterfaceInfosUsingPost,
  deleteInterfaceInfoUsingPost,
  listInterfaceInfoByPageUsingPost,
  updateInterfaceInfoUsingPost,
} from '@/services/backend-server/interfaceInfoController';
import { ExclamationCircleFilled, PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { FooterToolbar, PageContainer, ProTable } from '@ant-design/pro-components';
import { Button, message, Modal } from 'antd';
import React, { useRef, useState } from 'react';
import CreateModal from './components/CreateModal';
import UpdateModal from './components/UpdateModal';
import './index.less';

const { confirm } = Modal;

const TableList: React.FC = () => {
  const [createModalOpen, handleModalOpen] = useState<boolean>(false);

  const [updateModalOpen, handleUpdateModalOpen] = useState<boolean>(false);

  const [showDetail, setShowDetail] = useState<boolean>(false);

  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<API.InterfaceInfo>();
  const [selectedRowsState, setSelectedRows] = useState<API.InterfaceInfo[]>([]);

  // 添加
  const handleAdd = async (fields: API.InterfaceInfo) => {
    const hide = message.loading('正在添加');
    try {
      await addInterfaceInfoUsingPost({ ...fields });
      hide();
      message.success('创建成功');
      handleModalOpen(false);
      return true;
    } catch (error: any) {
      hide();
      message.error('创建失败:' + error.message);
      return false;
    }
  };

  // 修改
  const handleUpdate = async (fields: API.InterfaceInfo) => {
    const hide = message.loading('修改中');
    try {
      await updateInterfaceInfoUsingPost({
        ...fields,
      });
      hide();

      message.success('操作成功');
      return true;
    } catch (error: any) {
      hide();
      message.error('操作失败' + error.message);
      return false;
    }
  };

  // 删除多个数据
  const handleRemove = async (items: API.InterfaceInfo[]) => {
    let ids = items.map((item) => item.id as number).filter((id) => id !== undefined);
    let delReq: API.InterfaceDeleteRequest = {
      ids: ids,
    };
    const hide = message.loading('删除中');
    try {
      hide();
      await deleteInterfaceInfosUsingPost(delReq);
      message.success('删除成功');
      return true;
    } catch (err: any) {
      hide();
      message.error('删除异常' + err.message);
      return false;
    }
  };

  // 删除单个数据
  const handleRemoveOne = async (item: API.InterfaceInfo) => {
    const hide = message.loading('删除中');
    try {
      hide();
      await deleteInterfaceInfoUsingPost({ id: item.id });
      message.success('删除成功');
      return true;
    } catch (err: any) {
      hide();
      message.error('删除失败' + err.message);
      return false;
    }
  };

  const columns: ProColumns<API.InterfaceInfo>[] = [
    {
      title: 'id',
      dataIndex: 'id',
      valueType: 'index',
      // fixed: 'left',
      width: 80,
    },
    {
      title: '接口名称',
      dataIndex: 'name',
      valueType: 'text',
      // fixed: 'left',
      width: 160,
    },
    {
      title: '描述',
      dataIndex: 'description',
      valueType: 'textarea',
      ellipsis: true,
      width: 300,
    },
    {
      title: '请求方法',
      dataIndex: 'method',
      valueType: 'text',
      width: 80,
    },
    {
      title: '请求地址',
      dataIndex: 'url',
      valueType: 'text',
      copyable: true,
      ellipsis: true,
      width: 260,
    },
    {
      title: '请求头',
      dataIndex: 'requestHeader',
      valueType: 'textarea',
      ellipsis: true,
      copyable: true,
      width: 360,
      hideInSearch: true,
    },
    {
      title: '响应头',
      dataIndex: 'responseHeader',
      valueType: 'textarea',
      ellipsis: true,
      copyable: true,
      width: 360,
      hideInSearch: true,
    },
    {
      title: '接口状态',
      dataIndex: 'status',
      hideInForm: true,

      valueEnum: {
        0: {
          text: '关闭',
          status: 'Default',
        },
        1: {
          text: '开启',
          status: 'Processing',
        },
      },
      width: 80,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      valueType: 'dateTime',
      hideInForm: true,
      ellipsis: true,
      width: 160,
      hideInSearch: true,
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
      valueType: 'dateTime',
      hideInForm: true,
      ellipsis: true,
      width: 160,
      hideInSearch: true,
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      width: 160,
      render: (_, record) => [
        <a
          key="edit"
          onClick={() => {
            handleUpdateModalOpen(true);
            setCurrentRow(record);
          }}
          className="warning-text"
        >
          修改
        </a>,
        <a key="detail" href="https://procomponents.ant.design/">
          查看
        </a>,
        <a
          key="delete"
          className="danger-text"
          onClick={() => {
            confirm({
              title: '操作警告',
              icon: <ExclamationCircleFilled />,
              content: '数据删除后不可逆，确定要删除选中的这条数据吗？',
              okText: '确定',
              okType: 'danger',
              cancelText: '取消',
              onOk: async () => {
                const success = await handleRemoveOne(record);
                if (success) {
                  if (actionRef.current) {
                    actionRef.current.reload();
                  }
                }
              },
              onCancel() {
                console.log('Cancel');
              },
            });
          }}
        >
          删除
        </a>,
      ],
    },
  ];

  type PageParams = {
    current: number;
    pageSize: number;
  };
  return (
    <PageContainer>
      <ProTable<API.InterfaceInfo, PageParams>
        scroll={{
          x: 2400,
        }}
        cardBordered={{
          table: true,
        }}
        headerTitle="接口详情"
        actionRef={actionRef}
        rowKey="id"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <Button
            type="primary"
            key="primary"
            onClick={() => {
              handleModalOpen(true);
            }}
          >
            <PlusOutlined /> 新建
          </Button>,
        ]}
        request={async (params) => {
          const res = await listInterfaceInfoByPageUsingPost({
            ...params,
          });
          if (res.data) {
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
        rowSelection={{
          onChange: (_, selectedRows) => {
            setSelectedRows(selectedRows);
          },
        }}
        pagination={{
          defaultCurrent: 1,
          defaultPageSize: 10,
          showTotal: (total) => `共 ${total} 条`,
          showSizeChanger: true,
          pageSizeOptions: ['10', '50', '100'],
        }}
      />
      {selectedRowsState?.length > 0 && (
        <FooterToolbar
          extra={
            <div>
              已选择 <a style={{ fontWeight: 600 }}>{selectedRowsState.length}</a> 项 &nbsp;&nbsp;
            </div>
          }
        >
          <Button
            onClick={() => {
              confirm({
                title: '操作警告',
                icon: <ExclamationCircleFilled />,
                content: '数据删除后不可逆，确定要删除选中的这些数据吗？',
                okText: '确定',
                okType: 'danger',
                cancelText: '取消',
                onOk: async () => {
                  await handleRemove(selectedRowsState);
                  setSelectedRows([]);
                  actionRef.current?.reloadAndRest?.();
                },
                onCancel() {
                  console.log('Cancel');
                },
              });
            }}
            className="danger-bg"
          >
            批量删除
          </Button>
        </FooterToolbar>
      )}

      {/* 添加接口对话框 */}
      <CreateModal
        columns={columns}
        onCancel={() => {
          handleModalOpen(false);
        }}
        onSubmit={async (values) => {
          const success = await handleAdd(values);
          if (success) {
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
        visible={createModalOpen}
      />

      {/* 修改接口对话框 */}
      <UpdateModal
        columns={columns}
        visible={updateModalOpen}
        initValue={currentRow || {}}
        onSubmit={async (value) => {
          const success = await handleUpdate({ ...value, id: currentRow?.id });
          if (success) {
            handleUpdateModalOpen(false);
            setCurrentRow(undefined);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
        onCancel={() => {
          handleUpdateModalOpen(false);
          if (!showDetail) {
            setCurrentRow(undefined);
          }
        }}
      />
    </PageContainer>
  );
};

export default TableList;

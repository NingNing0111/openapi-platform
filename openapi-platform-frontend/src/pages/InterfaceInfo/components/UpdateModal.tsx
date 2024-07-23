import { ProColumns, ProFormInstance, ProTable } from '@ant-design/pro-components';

import { Modal } from 'antd';
import { useEffect, useRef } from 'react';
export type Props = {
  initValue: API.InterfaceInfo;
  columns: ProColumns<API.InterfaceInfo>[];
  onCancel: () => void;
  onSubmit: (values: API.InterfaceInfo) => Promise<void>;
  visible: boolean;
};

const UpdateModal: React.FC<Props> = (props: Props) => {
  const { initValue, columns, visible, onCancel, onSubmit } = props;

  const formRef = useRef<ProFormInstance>();

  useEffect(() => {
    if (formRef) {
      formRef.current?.setFieldsValue(initValue);
    }
  }, [initValue]);

  return (
    <Modal open={visible} onCancel={() => onCancel?.()} footer={null}>
      <ProTable
        form={{
          initialValues: initValue,
        }}
        formRef={formRef}
        type="form"
        columns={columns}
        onSubmit={async (value) => {
          onSubmit?.(value);
        }}
      />
    </Modal>
  );
};

export default UpdateModal;

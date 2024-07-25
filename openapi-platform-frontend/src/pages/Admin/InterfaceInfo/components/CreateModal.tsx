import { ProColumns, ProTable } from '@ant-design/pro-components';

import { Modal } from 'antd';
export type Props = {
  columns: ProColumns<API.InterfaceInfo>[];
  onCancel: () => void;
  onSubmit: (values: API.InterfaceInfo) => Promise<void>;
  visible: boolean;
};

const CreateModal: React.FC<Props> = (props: Props) => {
  const { columns, visible, onCancel, onSubmit } = props;

  return (
    <Modal open={visible} onCancel={() => onCancel?.()} footer={null}>
      <ProTable
        type="form"
        columns={columns}
        onSubmit={async (value) => {
          onSubmit?.(value);
        }}
      />
    </Modal>
  );
};

export default CreateModal;

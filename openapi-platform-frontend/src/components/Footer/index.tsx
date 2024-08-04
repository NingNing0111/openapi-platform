import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import React from 'react';

const Footer: React.FC = () => {
  return (
    <DefaultFooter
      copyright="PG Thinker"
      style={{
        background: 'none',
      }}
      links={[
        {
          key: 'github',
          title: <GithubOutlined />,
          href: 'https://github.com/ningning0111/openapi-platform',
          blankTarget: true,
        },
        {
          key: 'PG Thinker',
          title: "PG Thinker's Blog",
          href: 'https://pgthinker.me',
          blankTarget: true,
        },
      ]}
    />
  );
};

export default Footer;

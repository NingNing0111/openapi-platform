import { Footer } from '@/components';
import { sendVerifyCodeUsingPost } from '@/services/backend-server/mailController';
import { userRegisterUsingPost } from '@/services/backend-server/userController';
import { LockOutlined, MailOutlined, SendOutlined, UserOutlined } from '@ant-design/icons';
import { CaptFieldRef, LoginForm, ProFormCaptcha, ProFormText } from '@ant-design/pro-components';
import { Helmet } from '@umijs/max';
import { Alert, message, Tabs } from 'antd';
import { createStyles } from 'antd-style';
import { useRef, useState } from 'react';
import Settings from '../../../../config/defaultSettings';
const useStyles = createStyles(({ token }) => {
  return {
    action: {
      marginLeft: '8px',
      color: 'rgba(0, 0, 0, 0.2)',
      fontSize: '24px',
      verticalAlign: 'middle',
      cursor: 'pointer',
      transition: 'color 0.3s',
      '&:hover': {
        color: token.colorPrimaryActive,
      },
    },
    lang: {
      width: 42,
      height: 42,
      lineHeight: '42px',
      position: 'fixed',
      right: 16,
      borderRadius: token.borderRadius,
      ':hover': {
        backgroundColor: token.colorBgTextHover,
      },
    },
    container: {
      display: 'flex',
      flexDirection: 'column',
      height: '100vh',
      overflow: 'auto',
      backgroundImage:
        "url('https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/V-_oS6r-i7wAAAAAAAAAAAAAFl94AQBr')",
      backgroundSize: '100% 100%',
    },
  };
});

const RegisterMessage: React.FC<{ content: string }> = ({ content }) => {
  return (
    <Alert
      style={{
        marginBottom: 24,
      }}
      message={content}
      type="error"
      showIcon
    />
  );
};

const Register: React.FC = () => {
  const [type, setType] = useState<string>('email');
  const [userRegisterState, setUserRegisterState] = useState<API.UserRegisterRequest>({});
  const { styles } = useStyles();
  const [userAccount, setUserAccount] = useState('');
  const handleSubmit = async (userRegisterInfo: API.UserRegisterRequest) => {
    try {
      // 中国注册
      const res = await userRegisterUsingPost(userRegisterInfo);

      if (res.data) {
        const defaultLoginSuccessMessage = '注册成功！';
        message.success(defaultLoginSuccessMessage);
        return;
      } else {
        message.error(res.message);
      }
    } catch (error) {
      const defaultLoginFailureMessage = '注册失败，请重试！';
      console.log(error);
      message.error(defaultLoginFailureMessage);
    }
  };
  const captchaRef = useRef<CaptFieldRef | null | undefined>();
  return (
    <div className={styles.container}>
      <Helmet>
        <title>
          {'注册'}- {Settings.title}
        </title>
      </Helmet>
      <div
        style={{
          flex: '1',
          padding: '32px 0',
        }}
      >
        <LoginForm
          contentStyle={{
            minWidth: 280,
            maxWidth: '75vw',
          }}
          submitter={{
            searchConfig: {
              submitText: '注册',
            },
          }}
          logo={<img alt="logo" src="/logo.svg" />}
          title="PG Thinker's OpenAPI Platform"
          subTitle={'提供免费的API给开发者使用'}
          onFinish={async (values) => {
            await handleSubmit({ ...values, userAccount: userAccount });
          }}
        >
          <Tabs
            activeKey={type}
            onChange={(e) => {
              setType(e);
            }}
            centered
            items={[
              {
                key: 'email',
                label: '邮箱注册',
              },
            ]}
          />

          {!userRegisterState && type === 'email' && <RegisterMessage content="验证码错误" />}
          {type === 'email' && (
            <>
              <ProFormText
                fieldProps={{
                  size: 'large',
                  prefix: <UserOutlined />,
                }}
                name="userAccount"
                placeholder={'账户'}
                getValueFromEvent={(e) => {
                  setUserAccount(e.target.value);
                }}
                rules={[
                  ({ getFieldValue }) => ({
                    validator(rule, value) {
                      if (userAccount && userAccount !== '') {
                        return Promise.resolve();
                      }
                      return Promise.reject('账户是必填项！');
                    },
                  }),
                ]}
              />
              <ProFormText
                fieldProps={{
                  size: 'large',
                  prefix: <MailOutlined />,
                }}
                name="email"
                placeholder={'邮箱'}
                rules={[
                  {
                    required: true,
                    message: '邮箱是必填项！',
                  },
                  {
                    pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
                    message: '不合法的邮箱号',
                  },
                ]}
              />
              <ProFormCaptcha
                fieldRef={captchaRef}
                fieldProps={{
                  size: 'large',
                  prefix: <SendOutlined />,
                }}
                captchaProps={{
                  size: 'large',
                }}
                phoneName="email"
                placeholder={'验证码'}
                captchaTextRender={(timing, count) => {
                  if (timing) {
                    return `${count} ${'秒后重新获取'}`;
                  }
                  return '获取验证码';
                }}
                name="verifyCode"
                rules={[
                  {
                    required: true,
                    message: '验证码是必填项！',
                  },
                ]}
                onGetCaptcha={async (email) => {
                  if (!email || email === '') {
                    message.error('邮箱为空');
                    captchaRef.current?.endTiming();
                    return;
                  }
                  if (!userAccount || userAccount === '') {
                    message.error('用户名为空');
                    captchaRef.current?.endTiming();
                    return;
                  }
                  const result = await sendVerifyCodeUsingPost({
                    email: email,
                    userAccount: userAccount,
                  });

                  if (result.code === 0) {
                    message.success(result.data);
                  } else {
                    captchaRef.current?.endTiming();
                    message.error(result.message);
                  }
                }}
              />
              <ProFormText.Password
                name="userPassword"
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined />,
                }}
                placeholder={'密码'}
                rules={[
                  {
                    required: true,
                    message: '密码是必填项！',
                  },
                ]}
              />
              <ProFormText.Password
                name="checkPassword"
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined />,
                }}
                placeholder={'确认密码'}
                rules={[
                  {
                    required: true,
                    message: '密码是必填项！',
                  },
                  ({ getFieldValue }) => ({
                    validator(rule, value) {
                      if (!value || getFieldValue('userPassword') === value) {
                        return Promise.resolve();
                      }
                      return Promise.reject('新密码与确认新密码不同！');
                    },
                  }),
                ]}
              />
            </>
          )}

          <div
            style={{
              marginBottom: 24,
            }}
          >
            <a
              style={{
                float: 'right',
              }}
              href="/user/login"
            >
              前往登录
            </a>
          </div>
        </LoginForm>
      </div>
      <Footer />
    </div>
  );
};

export default Register;

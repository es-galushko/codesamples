import React from 'react';
import { useAsyncFn } from 'react-use';
import { useHistory } from 'react-router-dom';
import { toast } from 'react-toastify';
import { useTranslation } from 'react-i18next';
import * as Sentry from '@sentry/browser';
import { useDispatch } from 'react-redux';

import { useAmplitudePageVisit } from 'hooks/amplitude';
import { LOGIN } from 'constants/amplitude/pageTitles';
import { loginAzure, login } from 'http/auth';
import AmplitudeService from 'services/amplitude';
import { isProduction } from 'services/environment';
import { getError } from 'utils/error';
import { routes } from 'services/router';
import { actions } from 'redux/auth';
import { USER_LOGIN } from 'constants/amplitude/events';

import Footer from 'components/Footer';
import Header from 'components/Header';

import Form from './Form';
import { LoginData } from './Form/validationSchema';

import * as S from './styled';

const AZURE_LOGIN_URL = loginAzure();

const Login = () => {
  const { t } = useTranslation('signIn');
  const history = useHistory();
  const dispatch = useDispatch();

  const [, authenticate] = useAsyncFn(async (values?: LoginData) => {
    try {
      if (!values) {
        return;
      }

      const data = await login(values);

      dispatch(actions.authenticate(data));

      AmplitudeService.setUser({ id: data.user.id, email: data.user.email });
      AmplitudeService.logEvent(USER_LOGIN);

      history.push(routes.home);
    } catch (err) {
      Sentry.captureException(err);
      toast.error(getError(err).error);
    }
  }, []);

  useAmplitudePageVisit(LOGIN);

  return (
    <div>
      <S.Container>
        <Header isNavbarHidden={true} isSubHeaderHidden={true} />
        <S.Wrapper>
          <S.Text>
            <S.Bold>
              {t('title')}
              <br />
              {t('titleNewLine')}
            </S.Bold>
            {t('subTitle')}
            <br />
            {t('subTitleNewLine')}
          </S.Text>
          {!isProduction ? (
            <>
              <Form onSubmit={authenticate} />
              <S.Link to={routes.authModule.forgotPassword}>{t('forgotPassword')}</S.Link>
              <S.SeparatorLine />
            </>
          ) : null}
          <S.Button as="a" href={AZURE_LOGIN_URL}>
            {isProduction ? t('form.submitButton') : t('azureSignIn')}
          </S.Button>
        </S.Wrapper>
      </S.Container>
      <Footer />
    </div>
  );
};

export default Login;

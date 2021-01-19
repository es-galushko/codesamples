import React from 'react';
import { useTranslation } from 'react-i18next';
import { useDispatch, useSelector } from 'react-redux';

import GradientSwitch from '@/components/GradientSwitch';
import { SettingsScreenNavigationProp } from '@/navigator';
import { openOtherApps, rateUs, contactUs, getAppVersion } from '@/utils/app';
import { getHapticEnabled, getStartScreenOnAppLaunch } from '@/redux/settings/selectors';
import { actions } from '@/redux/settings';
import { getLanguages } from '@/locales/utils';

import BottomControls from '@/components/BottomControls';

import * as S from './styled';

import ActionOption from './ActionOption';
import AppStartOption from './AppStartOption';

type Props = {
  navigation: SettingsScreenNavigationProp;
};

const appVersion = getAppVersion();

const Settings = ({ navigation }: Props) => {
  const dispatch = useDispatch();
  const { t, i18n } = useTranslation(['commom', 'settings']);
  const languages = getLanguages();
  const activeLanguage = languages.find(lang => lang.key === i18n.language);

  const startScreen = useSelector(getStartScreenOnAppLaunch);
  const isHapticEnabled = useSelector(getHapticEnabled);

  const onBackPressed = () => navigation.goBack();

  const onHapticToggle = () => dispatch(actions.toggleHaptic());

  const onLanguagesPress = () => navigation.navigate('Languages');

  const onAppStartSettingsPress = () => navigation.navigate('StartScreenSettings');

  return (
    <S.Container>
      <S.InnerContainer>
        <S.Block>
          <S.Touchable onPress={contactUs}>
            <S.Label>{t('settings:buttons.contactUs')}</S.Label>
          </S.Touchable>
          <S.TouchableSeparator />
          <S.Touchable onPress={rateUs}>
            <S.Label>{t('settings:buttons.rateUs')}</S.Label>
          </S.Touchable>
          <S.TouchableSeparator />
          <S.Touchable onPress={openOtherApps}>
            <S.Label>{t('settings:buttons.otherApps')}</S.Label>
          </S.Touchable>
        </S.Block>
        <S.BlocksSeparator />
        <S.Block>
          <ActionOption
            rightControl={<GradientSwitch isChecked={isHapticEnabled} onChange={onHapticToggle} />}
            icon="haptic"
            label={t('settings:buttons.useHaptic')}
          />
          <S.TouchableSeparator />
          <S.BaseTouchable onPress={onLanguagesPress}>
            <ActionOption
              rightControl={<S.LanguageLabel>{activeLanguage?.label}</S.LanguageLabel>}
              icon="languages"
              label={t('settings:buttons.language')}
            />
          </S.BaseTouchable>
          <S.TouchableSeparator />
          <AppStartOption
            topTitle={t('settings:buttons.appStart.label')}
            bottomTitle={t(`settings:appStartOptions.${startScreen}`)}
            onPress={onAppStartSettingsPress}
          />
        </S.Block>
      </S.InnerContainer>
      <S.AppInfoContainer>
        <S.AppInfoLabel>{t('common:appName')}</S.AppInfoLabel>
        <S.AppInfoLabel>{appVersion}</S.AppInfoLabel>
      </S.AppInfoContainer>
      <BottomControls backTitle={t('settings:buttons.back')} onBack={onBackPressed} />
    </S.Container>
  );
};

export default Settings;

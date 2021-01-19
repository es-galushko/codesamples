import React from 'react';
import { useTranslation } from 'react-i18next';

import DialogModal from 'components/DialogModal';
import Loader from 'components/Loader';
import Button from 'components/Button';

import * as S from './styled';

type DeleteDialogProps = {
  isOpen: boolean;
  isLoading: boolean;
  userId: number | undefined;
  onClose: () => void;
  onSubmit: (id: number) => void;
} & DefaultProps;

type DefaultProps = {
  fullName?: string;
};

const defaultProps: DefaultProps = {
  fullName: '',
};

const DeleteDialog = ({
  isOpen,
  isLoading,
  userId,
  fullName,
  onClose,
  onSubmit,
}: DeleteDialogProps) => {
  const { t } = useTranslation('manageUsers');

  const handleConfirm = () => {
    if (userId) {
      onSubmit(userId);
    }
  };

  const handleClose = () => {
    onClose();
  };

  return (
    <DialogModal
      title={t('deleteDialog.title')}
      isOpen={isOpen}
      onClose={handleClose}
      onRequestClose={handleClose}
    >
      <>
        <S.Text>
          {t('deleteDialog.text')} {fullName}
        </S.Text>
        {isLoading ? (
          <Loader />
        ) : (
          <S.Buttons>
            <S.Buttom>
              <Button text={t('deleteDialog.submit')} sizeText={14} onClick={handleConfirm} />
            </S.Buttom>
            <S.Buttom>
              <Button
                text={t('cancel')}
                sizeText={14}
                themeButton="darkGray"
                onClick={handleClose}
              />
            </S.Buttom>
          </S.Buttons>
        )}
      </>
    </DialogModal>
  );
};

DeleteDialog.defaultProps = defaultProps;

export default DeleteDialog;

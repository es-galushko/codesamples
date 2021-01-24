from __future__ import unicode_literals
from django.conf import settings

from django.db import models
from django.utils.encoding import smart_text as smart_unicode
from django.utils.translation import ugettext_lazy as _


class Book(models.Model):
    user = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE)
    name = models.CharField(_("Name"), max_length=255, blank=False, null=False)
    author = models.CharField(_("Author"), max_length=255, blank=False, null=False)
    date_of_writing = models.DateTimeField(_("Date of writing"), blank=False, null=False)

    class Meta:
        verbose_name = _("Book")
        verbose_name_plural = _("Books")

    def __unicode__(self):
        return smart_unicode(self.name)

<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
<add>
    <xsl:for-each select="FILE/PAGE">
    <doc>
      <field name="id"><xsl:value-of select="id"/></field>
	  <field name="url"><xsl:value-of select="URL"/></field>
	  <field name="meta"><xsl:value-of select="META"/></field>
	  <field name="title"><xsl:value-of select="TITLE"/></field>
	  <field name="body"><xsl:value-of select="BODY"/></field>
	  <field name="lastmodified"><xsl:value-of select="LASTMODIFIED"/></field>
    </doc>
    </xsl:for-each>
</add>
</xsl:template>
</xsl:stylesheet> 
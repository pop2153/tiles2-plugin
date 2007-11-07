<%@ page session="false" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<table  border="2"  width="300">
  <tr>
    <td  bgcolor="Blue"><strong><tiles:getAsString name="title"/></strong></td>
  </tr>
  <tr>
    <td><tiles:insertAttribute name="header"/></td>
  </tr>
  <tr>
    <td><tiles:insertAttribute name="body"/></td>
  </tr>
</table>
package org.greenscape.site.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.greenscape.core.model.Page;
import org.greenscape.core.model.PageModel;
import org.greenscape.core.model.SiteModel;
import org.greenscape.core.service.Service;
import org.greenscape.site.service.SiteService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component
public class SiteServiceImpl implements SiteService {

	private final static String MODEL_NAME = "site";
	private Service service;

	@Override
	public List<SiteModel> find() {
		return service.find(MODEL_NAME);
	}

	@Override
	public SiteModel find(String id) {
		return service.findByModelId(MODEL_NAME, id);
	}

	@Override
	public List<SiteModel> find(Map<String, List<String>> properties) {
		return service.find(MODEL_NAME, properties);
	}

	@Override
	public List<SiteModel> find(String propertyName, Object value) {
		return service.find(MODEL_NAME, propertyName, value);
	}

	@Override
	public Collection<SiteModel> findByOrganizationId(String orgId) {
		return service.find(MODEL_NAME, SiteModel.ORGANIZATION_ID, orgId);
	}

	@Override
	public SiteModel save(SiteModel model) {
		Subject subject = SecurityUtils.getSubject();
		if (!subject.isPermitted("site:add:" + model.getModelId())) {
			throw new AuthorizationException();
		}
		return service.save(MODEL_NAME, model);
	}

	@Override
	public SiteModel update(SiteModel model) {
		Subject subject = SecurityUtils.getSubject();
		if (!subject.isPermitted("site:edit:" + model.getModelId())) {
			throw new AuthorizationException();
		}
		return service.update(MODEL_NAME, model);
	}

	@Override
	public void delete() {
		Subject subject = SecurityUtils.getSubject();
		if (!subject.isPermitted("site:delete")) {
			throw new AuthorizationException();
		}
		service.delete(MODEL_NAME);
	}

	@Override
	public void delete(String siteId) {
		Subject subject = SecurityUtils.getSubject();
		if (!subject.isPermitted("site:delete:" + siteId)) {
			throw new AuthorizationException();
		}
		service.delete(MODEL_NAME, siteId);
	}

	@Override
	public void deletePage(String siteId, String pageId) {
		// allow delete if no of pages > 1
		List<Page> pages = service.find("page", PageModel.SITE_ID, siteId);
		if (pages != null && pages.size() > 1) {
			service.delete("page", pageId);
		} else {
			throw new RuntimeException("Page deletion not allowed as site has single page");
		}
	}

	@Reference
	public void setService(Service service) {
		this.service = service;
	}

	public void unsetService(Service service) {
		this.service = null;
	}
}

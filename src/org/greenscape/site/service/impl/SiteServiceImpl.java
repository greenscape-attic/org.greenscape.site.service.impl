package org.greenscape.site.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.greenscape.core.service.Service;
import org.greenscape.persistence.DocumentModel;
import org.greenscape.site.Page;
import org.greenscape.site.PageModel;
import org.greenscape.site.Site;
import org.greenscape.site.SiteModel;
import org.greenscape.site.service.SiteService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component
public class SiteServiceImpl implements SiteService {

	private Service service;

	// private SitePersistence sitePersistence;

	@Override
	public <M extends DocumentModel> List<M> find(Class<? extends DocumentModel> clazz) {
		return service.find(clazz);
	}

	@Override
	public <M extends DocumentModel> M find(Class<? extends DocumentModel> clazz, String id) {
		return service.find(clazz, id);
	}

	@Override
	public <M extends DocumentModel> List<M> find(Class<? extends DocumentModel> clazz,
			Map<String, List<String>> properties) {
		return service.find(clazz, properties);
	}

	@Override
	public <M extends DocumentModel> List<M> find(Class<? extends DocumentModel> clazz, String propertyName,
			Object value) {
		return service.find(clazz, propertyName, value);
	}

	@Override
	public Site findBySiteId(String siteId) {
		return service.find(Site.class, siteId);
	}

	@Override
	public Collection<Site> findByOrganizationId(String orgId) {
		return service.find(Site.class, SiteModel.ORGANIZATION_ID, orgId);
	}

	@Override
	public <M extends DocumentModel> M save(M model) {
		return service.save(model);
	}

	@Override
	public <M extends DocumentModel> M update(M model) {
		return service.update(model);
	}

	@Override
	public Site save(Site site) {
		return service.save(site);
	}

	@Override
	public void delete(Class<? extends DocumentModel> clazz) {
		service.delete(clazz);
	}

	@Override
	public void delete(Class<? extends DocumentModel> clazz, String id) {
		service.delete(clazz, id);
	}

	@Override
	public void deleteBySiteId(String siteId) {
		service.delete(Site.class, siteId);
	}

	@Override
	public void deletePage(String siteId, String pageId) {
		// allow delete if no of pages > 1
		List<Page> pages = service.find(Page.class, PageModel.SITE_ID, siteId);
		if (pages != null && pages.size() > 1) {
			service.delete(Page.class, pageId);
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

	// @Reference
	// public void setSitePersistence(SitePersistence sitePersistence) {
	// this.sitePersistence = sitePersistence;
	// }
	//
	// public void unsetSitePersistence(SitePersistence sitePersistence) {
	// this.sitePersistence = null;
	// }
}

package com.appsdeveloperblog.photoapp.api.users.data;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.appsdeveloperblog.photoapp.api.users.ui.model.AlbumResponseModel;

import feign.FeignException;
import feign.hystrix.FallbackFactory;

@FeignClient(name = "albums-ws", fallbackFactory = AlbumsFallbackFactory.class)
public interface AlbumsServiceClient {

	@GetMapping("/users/{id}/albums")
	public List<AlbumResponseModel> getAlbums(@PathVariable String id);
}

@Component
class AlbumsFallbackFactory implements FallbackFactory<AlbumsServiceClient> {

	@Override
	public AlbumsServiceClient create(Throwable cause) {
		return new AlbumsServiceClientFallback(cause);
	}

}

class AlbumsServiceClientFallback implements AlbumsServiceClient {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private final Throwable cause;

	public AlbumsServiceClientFallback(Throwable cause) {
		this.cause = cause;
	}

	@Override
	public List<AlbumResponseModel> getAlbums(String id) {

		if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
			logger.error(new StringBuilder().append("404 error took place when getAlbums was called with userId: ").append(id).append(". Error message: ").append(cause.getLocalizedMessage()).toString());
		} else {
			logger.error(new StringBuilder().append("Other error took place: ").append(cause.getLocalizedMessage()).toString());
		}

		return new ArrayList<>();
	}

}

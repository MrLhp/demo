package com.example.demo.service.impl;

import com.example.demo.service.DataHelperService;
import com.example.demo.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class DataHelperServiceImpl implements DataHelperService {
	private static final String FILE_ENCODING = "UTF-8";
	private final DateFormat dateFormatForPath = new SimpleDateFormat("/yyyy/MM/");
	private final DateFormat dateFormatForSuffix = new SimpleDateFormat("-yyyyMMdd");
	private Calendar calendar = Calendar.getInstance(Locale.CHINA);

	private String rootDir="d:\\box";

	@Override
	public <T> void store(final String dataPrefix, final Date date, final T data) {
		this.store(dataPrefix, date, null, data);
	}

	@Override
	public <T> void store(final String dataPrefix, final Date date, final String cityCode, final T data) {
		final File file = buildFile(dataPrefix, date, cityCode);
		final String json = JsonUtils.pojoToJson(data);
		try {
			writeStringToFile(file, json, DataHelperServiceImpl.FILE_ENCODING);
		} catch (final IOException e) {
			final String msg = "文件写入失败: " + file.getAbsolutePath();
			DataHelperServiceImpl.log.warn(msg);
		}
	}

	@Override
	public <T> void store(String dataPrefix, List<T> data) {
		File file = buildFile(dataPrefix);
		String json = JsonUtils.pojoToJson(data);
		try {
			writeStringToFile(file, json, DataHelperServiceImpl.FILE_ENCODING);
			DataHelperServiceImpl.log.info("文件写入成功: " + file.getAbsolutePath());
		} catch (final IOException e) {
			final String msg = "文件写入失败: " + file.getAbsolutePath();
			DataHelperServiceImpl.log.warn(msg);
		}
	}

	@Override
	public <T> void store(String dataPrefix, T data, String... catalogs) {
		File file = buildFile(dataPrefix);
		String json = JsonUtils.pojoToJson(data);
		try{
			writeStringToFile(file, json, DataHelperServiceImpl.FILE_ENCODING);
			DataHelperServiceImpl.log.info("文件写入成功: " + file.getAbsolutePath());
		}catch (final IOException e) {
			final String msg = "文件写入失败: " + file.getAbsolutePath();
			DataHelperServiceImpl.log.warn(msg);
		}
	}

	@Override
	public <T> void storeOne(String dataPrefix, String filmCode, T data) {
		File file = buildFile(dataPrefix);
		String json = JsonUtils.pojoToJson(data);
		try {
			writeStringToFile(file, json, DataHelperServiceImpl.FILE_ENCODING);
			DataHelperServiceImpl.log.info("文件写入成功: " + file.getAbsolutePath());
		} catch (final IOException e) {
			final String msg = "文件写入失败: " + file.getAbsolutePath();
			DataHelperServiceImpl.log.warn(msg);
		}
	}

	public <T> void storeOneExt(String dataPrefix, String filmCode, T data) {

		File file = buildFile(dataPrefix, filmCode);
		String json = JsonUtils.pojoToJson(data);
		try {
			writeStringToFile(file, json, DataHelperServiceImpl.FILE_ENCODING);
		} catch (final IOException e) {
			final String msg = "文件写入失败: " + file.getAbsolutePath();
			DataHelperServiceImpl.log.warn(msg);
		}

	}

	@Override
	public <T> void storeExt(final String dataPrefix, final T data, final Date date, String... catalogs) {
		final File file = buildFile(dataPrefix, date, catalogs);
		final String json = JsonUtils.pojoToJson(data);
		try {
			writeStringToFile(file, json, DataHelperServiceImpl.FILE_ENCODING);
		} catch (final IOException e) {
			final String msg = "文件写入失败: " + file.getAbsolutePath();
			DataHelperServiceImpl.log.warn(msg);
		}
	}

	@Override
	public <T> void storeExtList(String dataPrefix, List<T> data, Date date, String... catalogs) {
		final File file = buildFile(dataPrefix, date, catalogs);
		final String json = JsonUtils.pojoToJson(data);
		try {
			writeStringToFile(file, json, DataHelperServiceImpl.FILE_ENCODING);
			DataHelperServiceImpl.log.info("文件写入成功: " + file.getAbsolutePath());
		} catch (final IOException e) {
			final String msg = "文件写入失败: " + file.getAbsolutePath();
			DataHelperServiceImpl.log.warn(msg);
		}

	}

	public <T> void storeExtList(String dataPrefix, List<T> data, Date date, boolean isDirDate, String... catalogs) {
		final File file = buildNoDateDirFile(dataPrefix, date, isDirDate, catalogs);
		final String json = JsonUtils.pojoToJson(data);
		try {
			FileUtils.writeStringToFile(file, json, DataHelperServiceImpl.FILE_ENCODING);
		} catch (final IOException e) {
			final String msg = "文件写入失败: " + file.getAbsolutePath();
			DataHelperServiceImpl.log.warn(msg);
		}
	}

	@Override
	public <K, V> void storeMap(String dataPrefix, Map<K, V> data) {
		File file = buildFile(dataPrefix);
		String json = JsonUtils.pojoToJson(data);
		try {
			writeStringToFile(file, json, DataHelperServiceImpl.FILE_ENCODING);
		} catch (final IOException e) {
			final String msg = "文件写入失败: " + file.getAbsolutePath();
			DataHelperServiceImpl.log.warn(msg);
		}
	}


	@Override
	public <T> T load(final String dataPrefix, final Date date, final Class<T> clazz) {
		return this.load(dataPrefix, date, null, clazz);
	}

	@Override
	public <T> T load(final String dataPrefix, final Date date, final String cityCode, final Class<T> clazz) {
		final File file = buildFile(dataPrefix, date, cityCode);
		try {
			final String json = readFileToString(file, DataHelperServiceImpl.FILE_ENCODING);
			return JsonUtils.jsonToPojo(json, clazz);
		} catch (final IOException e) {
			final String msg = "文件读取失败: " + file.getAbsolutePath();
			DataHelperServiceImpl.log.warn(msg);
			return null;
		}
	}



	@Override
	public <T> T loadExt(final String dataPrefix, final Class<T> clazz, final Date date, String... catalogs) {
		final File file = buildFile(dataPrefix, date, catalogs);
		try {
			final String json = readFileToString(file, DataHelperServiceImpl.FILE_ENCODING);
			return JsonUtils.jsonToPojo(json, clazz);
		} catch (final IOException e) {
			final String msg = "文件读取失败: " + file.getAbsolutePath();
			DataHelperServiceImpl.log.warn(msg);
			return null;
		}
	}

	private File buildFile(final String dataPrefix, final Date date, final String... catalogs) {

		final StringBuffer sb = new StringBuffer();
		sb.append(rootDir);
		if (date != null) {
			sb.append(formatDateForPath(date)); // "/yyyy/MM/"
		}
		if (date != null) {
			sb.append(dataPrefix).append("/");
		} else {
			sb.append("/").append(dataPrefix).append("/");
		}
		if (catalogs != null && catalogs.length > 0) {
			for (final String catalog : catalogs) {
				if (StringUtils.isNotBlank(catalog)) {
					sb.append(catalog).append("/");
				}
			}
		}
		final String path = sb.toString();

		sb.append(dataPrefix);
		if (date != null) {
			sb.append(formatDateForSuffix(date)); // "-yyyyMMdd"
		}
		sb.append(".json");

		final String filepath = sb.toString();

		final File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		final File file = new File(filepath);
		return file;
	}

	private synchronized String formatDateForPath(final Date date) {
		return dateFormatForPath.format(date);
	}

	private synchronized String formatDateForSuffix(final Date date) {
		return dateFormatForSuffix.format(date);
	}

	@Override
	public <T> List<T> loadExtList(String dataPrefix, Class<T> clazz, Date date, String... catalogs) {
		final File file = buildFile(dataPrefix, date, catalogs);
		try {
			final String json = readFileToString(file, DataHelperServiceImpl.FILE_ENCODING);
			return JsonUtils.jsonToPojoList(json, new TypeReference<List<T>>() {
			});
		} catch (final IOException e) {
			final String msg = "文件读取失败: " + file.getAbsolutePath();
			DataHelperServiceImpl.log.warn(msg);
			return null;
		}
	}



	@Override
	public <T> List<T> load(String dataPrefix, Class<T> clazz) {
		File file = buildFile(dataPrefix);
		try {
			final String json = readFileToString(file, DataHelperServiceImpl.FILE_ENCODING);
			return JsonUtils.jsonToPojoList(json, new TypeReference<List<T>>() {
			});
		} catch (final IOException e) {
			final String msg = "文件读取失败: " + file.getAbsolutePath();
			DataHelperServiceImpl.log.warn(msg);
			return null;
		}

	}

	@Override
	public <T> T load(String dataPrefix, Class<T> clazz, String... catalogs) {
		return null;
	}

	private File buildFile(final String dataPrefix) {

		final StringBuffer sb = new StringBuffer();
		sb.append(rootDir);
		sb.append("/").append(dataPrefix).append("/");
		final String path = sb.toString();
		sb.append(dataPrefix);
		sb.append(".json");

		final String filepath = sb.toString();

		final File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		final File file = new File(filepath);
		return file;

	}

	@SuppressWarnings("unused")
	private File buildNoDateFile(final String dataPrefix, String... catalogs) {

		final StringBuffer sb = new StringBuffer();
		sb.append(rootDir);
		sb.append("/").append(dataPrefix).append("/");
		if (catalogs != null && catalogs.length > 0) {
			for (final String catalog : catalogs) {
				if (StringUtils.isNotBlank(catalog)) {
					sb.append(catalog).append("/");
				}
			}
		}
		final String path = sb.toString();
		sb.append(dataPrefix);
		sb.append(".json");

		final String filepath = sb.toString();

		final File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		final File file = new File(filepath);
		return file;

	}





	@Override
	public <T> T loadOne(String dataPrefix, String filmCode, Class<T> clazz) {
		File file = buildFile(dataPrefix, null, filmCode);
		try {
			final String json = readFileToString(file, DataHelperServiceImpl.FILE_ENCODING);
			return JsonUtils.jsonToPojo(json, clazz);
		} catch (final IOException e) {
			final String msg = "文件读取失败: " + file.getAbsolutePath();
			DataHelperServiceImpl.log.warn(msg);
			return null;
		}
	}

	public String writeStringToFile(File file, String data, String encoding) throws IOException {
		FileUtils.writeStringToFile(file, data, encoding);
		return data;
	}

	public String readFileToString(File file, String encoding) throws IOException {
		String returnVal = FileUtils.readFileToString(file, encoding);
		return returnVal;
	}





	/**
	 * 构建不带日期上层目录的带日期文件
	 *
	 * @param dataPrefix
	 * @param date
	 * @param catalogs
	 * @return
	 */
	private File buildNoDateDirFile(final String dataPrefix, final Date date, boolean isDateDir,
			final String... catalogs) {

		final StringBuffer sb = new StringBuffer();
		sb.append(rootDir);
		if (isDateDir) {
			if (date != null) {
				sb.append(formatDateForPath(date)); // "/yyyy/MM/"
				sb.append(dataPrefix).append("/");
			} else {
				sb.append("/").append(dataPrefix).append("/");
			}
		} else {
			sb.append("/").append(dataPrefix).append("/");
		}

		if (catalogs != null && catalogs.length > 0) {
			for (final String catalog : catalogs) {
				if (StringUtils.isNotBlank(catalog)) {
					sb.append(catalog).append("/");
				}
			}
		}
		final String path = sb.toString();

		sb.append(dataPrefix);
		if (date != null) {
			sb.append(formatDateForSuffix(date)); // "-yyyyMMdd"
		}
		sb.append(".json");

		final String filepath = sb.toString();

		final File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		final File file = new File(filepath);
		return file;
	}

	private File buildFile(final String dataPrefix, final String fileIdentify) {

		final StringBuffer sb = new StringBuffer();
		sb.append(rootDir);
		sb.append("/").append(dataPrefix).append("/");
		final String path = sb.toString();
		sb.append(dataPrefix);
		if (StringUtils.isNotBlank(fileIdentify)) {
			sb.append("-" + fileIdentify);
		}
		sb.append(".json");

		final String filepath = sb.toString();

		final File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		final File file = new File(filepath);
		return file;

	}

	private File buildFileDir(final String dataPrefix) {

		final StringBuffer sb = new StringBuffer();
		sb.append(rootDir);
		sb.append("/").append(dataPrefix).append("/");
		final String path = sb.toString();
		final File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		return dir;

	}

	@Override
	public <T> List<T> loadExtList(String dataPrefix, Class<T> clazz, Date date, boolean isDirDate,
			String... catalogs) {
		final File file = buildNoDateDirFile(dataPrefix, date, isDirDate, catalogs);
		try {
			final String json = readFileToString(file, DataHelperServiceImpl.FILE_ENCODING);
			return JsonUtils.jsonToPojoList(json, new TypeReference<List<T>>() {
			});
		} catch (final Exception e) {
			final String msg = "文件读取失败: " + file.getAbsolutePath();
			DataHelperServiceImpl.log.warn(msg);
			return null;
		}
	}


	@Override
	public <T> List<T> loadListFromDir(String dataPrefix, Class<T> clazz) {
		List<T> beans = new ArrayList<T>();
		final File file = buildFileDir(dataPrefix);
		try {
			File[] files = file.listFiles();
			if (files.length > 0) {
				for (File f : files) {
					final String json = readFileToString(f, DataHelperServiceImpl.FILE_ENCODING);
					beans.add(JsonUtils.jsonToPojo(json, clazz));
				}
			}
			return beans;
		} catch (final Exception e) {
			final String msg = "文件读取失败: " + file.getAbsolutePath();
			DataHelperServiceImpl.log.warn(msg);
			return null;
		}
	}

	@Override
	public <T> T loadOneExt(String dataPrefix, String filmCode, Class<T> clazz) {

		File file = buildFile(dataPrefix, filmCode);
		try {
			final String json = readFileToString(file, DataHelperServiceImpl.FILE_ENCODING);
			return JsonUtils.jsonToPojo(json, clazz);
		} catch (final Exception e) {
			final String msg = "文件读取失败: " + file.getAbsolutePath();
			DataHelperServiceImpl.log.warn(msg);
			return null;
		}
	}


	private static ObjectMapper mapper = new ObjectMapper();

	@Override
	@SuppressWarnings("unchecked")
	public <K, V> Map<K, V> loadMap(String dataPrefix, TypeReference<Map<K, V>> MAP_TYPE) {

		File file = buildFile(dataPrefix);
		try {
			final String json = readFileToString(file, DataHelperServiceImpl.FILE_ENCODING);
			return (Map<K, V>) mapper.readValue(json, MAP_TYPE);
		} catch (final IOException e) {
			final String msg = "文件读取失败: " + file.getAbsolutePath();
			DataHelperServiceImpl.log.warn(msg);
			return null;
		}

	}
}

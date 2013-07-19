/*******************************************************************************
 * Copyright 2013 Gengyu Shi
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.shigengyu.hyperion.utils;

import java.net.URL;
import java.util.jar.JarFile;

import org.reflections.Reflections;
import org.reflections.vfs.Vfs;
import org.reflections.vfs.Vfs.Dir;
import org.reflections.vfs.Vfs.UrlType;
import org.reflections.vfs.ZipDir;
import org.springframework.stereotype.Service;

@Service
public class ReflectionsHelper {

	public static class CustomUrlType implements UrlType {

		@Override
		public Dir createDir(final URL url) throws Exception {
			return new ZipDir(new JarFile(url.getFile()));
		}

		@Override
		public boolean matches(final URL url) throws Exception {
			// some jar files may not end with .jar
			return url.toExternalForm().contains(".jar");
		}

	}

	static {
		Vfs.addDefaultURLTypes(new CustomUrlType());
	}

	public static Reflections createReflections(final String... packageNames) {
		return new Reflections((Object[]) packageNames);
	}
}

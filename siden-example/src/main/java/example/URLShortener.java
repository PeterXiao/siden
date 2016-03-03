/*
 * Copyright 2014 SATO taichi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package example;

import ninja.siden.App;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author taichi
 */
public class URLShortener {

    public static void main(String[] args) {
        App app = new App();
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        app.post("/", (req, res) -> {
            Optional<String> opt = req.body();
            return opt.map(s -> {
                String k = Integer.toHexString(s.hashCode());
                map.put(k, s);
                return String.format("http://%s/%s", req.getRaw().getHostAndPort(), k);
            });
        });

        app.get("/:k", (req, res) -> req.params("k").map(key -> map.get(key))
                .map(res::redirect).orElse(404));

        app.listen();
    }
}

#!/bin/sh

backend_img_name=schedule-backend
dock_hub_URL=dockhub.gm.fh-koeln.de
dock_hub_username=dobrynin
dock_hub_img_location=${dock_hub_URL}/${dock_hub_username}/${backend_img_name}

build_backend() {
  docker build -t ${backend_img_name} .
}

clearDockerImages() {
  docker image rm $1
  docker image prune -f
}

uploadDockHub() {
  docker login ${dock_hub_URL} &&
  docker tag ${backend_img_name} ${dock_hub_img_location} &&
  docker push ${dock_hub_img_location} &&
  echo "successfully uploaded image ${backend_img_name} to ${dock_hub_URL}"
}

case "$1" in
"backend")
  clearDockerImages $backend_img_name &&
    build_backend &&
    docker-compose up -d &&
     exit 0
  ;;
"dockHub")
  clearDockerImages &&
    buildDockerImage &&
    uploadDockHub &&
     exit 0
  ;;
*)
  echo expected backend or dockHub, but was $1
  exit 1
  ;;
esac